package com.form76.generator.service.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailRequest {
  private String recipient;
  private String msgBody;
  private String subject;
  private String attachment;

}
