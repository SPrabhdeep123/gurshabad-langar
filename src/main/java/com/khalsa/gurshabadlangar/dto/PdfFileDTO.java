package com.khalsa.gurshabadlangar.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfFileDTO {
	
	 private Long id;

	    private String fileName;

	    private String filePath;

	private String title;

	private String description;


}
