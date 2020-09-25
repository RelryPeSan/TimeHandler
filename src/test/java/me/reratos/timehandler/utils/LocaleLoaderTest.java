package me.reratos.timehandler.utils;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocaleLoaderTest {

	@Test
	@DisplayName ("O valor das constantes de Messages deve existir no locale.properties (default)")
	void testGetStringStringObjectArray() {

		Field[] fields = null;
		
		// Recupera os campos da classe Messages
		try {
			fields = Class.forName("me.reratos.timehandler.utils.Messages").getFields(); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("Não foi possivel pegar os campos.");
		}
		
		assertNotNull(fields);
		assertTrue(fields.length > 0);
		
		File file = new File("src/main/resources/lang/locale.properties");
		FileInputStream fis = null;
		
		assertNotNull(file, "Arquivo locale padrão não encontrado!");
		
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			fail("Não foi possivel abrir o arquivo.");
		}
		
		LocaleLoader.initialize("en_US", fis);
		
//		System.out.println("---------------------------------------------");
		
		// Percorre todos os campos da classe Message
		for(Field f : fields) {
			String fieldName;
			String fieldValue;
			
			try {
				fieldName = f.getName();
				fieldValue = (String) f.get((Object)f);
				
				String message = LocaleLoader.getString(fieldValue);
				
//				System.out.println("final static: " + fieldName + " = " + fieldValue);
//				System.out.println("message: " + message);
//				System.out.println("---------------------------------------------");
				
				if(message == null || message.isEmpty()) {
					fail("Mensagem retornada vazia:\nField: " + fieldName + "\nValor: " + fieldValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("Erro ao tentar recuperar valor de field ou ao recuperar mensagem de LocaleLoader!");
			}
		}
	}

	@Test
	@DisplayName ("Os valores existentes na locale padrão deve existir na classe Messages")
	void testComparePropertiesWithMessages() {
		Properties properties = new Properties();
		Field[] fields = null;
		File file = new File("src/main/resources/lang/locale.properties");
		FileInputStream fis = null;
		List<String> listValueField = new ArrayList<>();
		List<String> listValueKey = new ArrayList<>();

		assertNotNull(file, "Arquivo locale padrão não encontrado!");
		
		// Carrega o arquivo de mensagens e carrega na properties
		try {
			fis = new FileInputStream(file);
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Não foi possivel abrir o arquivo!");
		}
		
		// Recupera os campos da classe Messages
		try {
			fields = Class.forName("me.reratos.timehandler.utils.Messages").getFields(); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("Não foi possivel pegar os campos!");
		}

		assertNotNull(fields);
		assertTrue(fields.length > 0);
		
		// Insere todos os valores dos fields em uma lista
		for(Field f : fields) {
			try {
				listValueField.add((String)f.get(f));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
				fail("Não foi possivel ler o valor do field: " + f.getName());
			}
		}
		
		// Insere todas as chaves do arquivo properties em uma lista
		for(Entry<Object, Object> e : properties.entrySet()) {
			listValueKey.add((String)e.getKey());
		}

		Collections.sort(listValueField);
		Collections.sort(listValueKey);
		
		assertLinesMatch(listValueField, listValueKey);
	}
}
