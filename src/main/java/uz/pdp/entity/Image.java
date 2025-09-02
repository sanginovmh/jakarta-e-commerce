package uz.pdp.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Image {
    private String id;
    private String fileName;
    private Long fileSize;
    private String fileLocation;
    private String suffix;
}
