package org.otp.otp.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileStorageResponse {
    private String filePath;
    private String fileEncodedContent;
}
