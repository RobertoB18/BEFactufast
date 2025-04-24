package com.flechaamarilla.facturacion.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

	public Boolean success;
	public String message;
	public Object data;
}
