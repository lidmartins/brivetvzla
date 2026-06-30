package com.brivetvzla.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsula la subida de fotos a S3 para las solicitudes (mascota perdida/encontrada).
 *
 * Convención de carpeta: el folder dentro del bucket es el PK de la solicitud,
 * rellenado con ceros a la izquierda hasta 8 dígitos. Ej: solicitud id=1 -> "00000001".
 *
 * Estructura final en el bucket:
 *   {bucket}/solicitudes/00000001/foto-1.jpg
 *   {bucket}/solicitudes/00000001/foto-2.jpg
 */
@Service
public class S3StorageService {

    private static final int FOLDER_DIGITS = 8;
    private static final String SOLICITUDES_PREFIX = "solicitudes";

    private final S3Client s3Client;
    private final String bucketName;
    private final String cloudFrontDomain;

    public S3StorageService(
            S3Client s3Client,
            @Value("${app.s3.bucket-name}") String bucketName,
            @Value("${app.s3.cloudfront-domain:}") String cloudFrontDomain) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.cloudFrontDomain = cloudFrontDomain;
    }

    /**
     * Convierte el PK numérico de la solicitud en el nombre de carpeta zero-padded.
     * Ej: 1 -> "00000001", 42 -> "00000042"
     */
    public String buildFolderName(Integer solicitudId) {
        return String.format("%0" + FOLDER_DIGITS + "d", solicitudId);
    }

    /**
     * Sube todas las fotos de una solicitud a S3 bajo su carpeta dedicada.
     *
     * @param solicitudId PK de la solicitud ya persistida (debe existir antes de llamar esto)
     * @param fotos       archivos recibidos en el multipart/form-data
     * @return objeto con el folder path relativo y la URL pública de la primera foto (main photo)
     */
    public UploadResult uploadFotos(Integer solicitudId, List<MultipartFile> fotos) {
        if (fotos == null || fotos.isEmpty()) {
            return UploadResult.empty();
        }

        String folder = buildFolderName(solicitudId);
        String folderPath = SOLICITUDES_PREFIX + "/" + folder;

        List<String> uploadedKeys = new ArrayList<>();

        for (int i = 0; i < fotos.size(); i++) {
            MultipartFile foto = fotos.get(i);
            if (foto.isEmpty()) continue;

            String extension = extractExtension(foto.getOriginalFilename());
            String key = folderPath + "/foto-" + (i + 1) + extension;

            try {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .contentType(foto.getContentType())
                                .build(),
                        RequestBody.fromInputStream(foto.getInputStream(), foto.getSize())
                );
                uploadedKeys.add(key);
            } catch (IOException e) {
                throw new UncheckedIOException("Error al leer la foto " + foto.getOriginalFilename(), e);
            }
        }

        if (uploadedKeys.isEmpty()) {
            return UploadResult.empty();
        }

        String mainPhotoUrl = buildPublicUrl(uploadedKeys.get(0));
        return new UploadResult(folderPath, mainPhotoUrl);
    }

    /**
     * Construye la URL pública de un objeto S3.
     * Usa CloudFront si está configurado; si no, la URL directa de S3.
     */
    private String buildPublicUrl(String key) {
        if (cloudFrontDomain != null && !cloudFrontDomain.isBlank()) {
            return "https://" + cloudFrontDomain + "/" + key;
        }
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    /**
     * Resultado de la subida: la ruta de carpeta (para guardar en BD)
     * y la URL pública de la foto principal.
     */
    public record UploadResult(String folderPath, String mainPhotoUrl) {
        public static UploadResult empty() {
            return new UploadResult("", "");
        }
    }
}
