package com.form76.generator.service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
  public String recipient;
  public String msgBody;
  public String subject;
  public String attachment;

}
