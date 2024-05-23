package cr.ac.una.app.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;

/**
 * Clase encargada del envio de correos
 * electronicos a los usuarios.
 * 
 * @author Allan Robinson
 * @author Isaac Herrera
 */

@Service
public class MailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	private static final String REMITENTE = "Mkniess032@gmail.com";

	/**
     * Método para enviar un correo electrónico simple.
     * @param mensaje Mensaje a enviar.
     * @param asunto Asunto del correo.
     * @param destinatario Correo al que
     * se le va a enviar el mensaje.
     */
    public void sendEmail(String mensaje, String asunto, String destinatario) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(destinatario);
        mailMessage.setFrom(REMITENTE);
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);
        mailSender.send(mailMessage);
    }

	/**
	 * Método para enviar un correo electrónico en forma de HTML.
	 * @param datos Lista con los datos que se van a introducir
	 * y el nombre de la variable que representa dicho dato en
	 * el HTML. La lista debe contener Strings con el siguiente
	 * formato: "${nombreVariable}, datoParaVariable".<br><br>
	 * Ejemplo: <br><br>
	 * <blockquote><pre>
	 *     List&lt;String&gt; datos = new ArrayList<>();
     *     datos.add("${userName}, Juan");
	 * </pre></blockquote>
	 * @param nombreHTML Nombre del archivo HTML que se va a enviar.
	 * @param asunto El asunto del correo.
	 * @param destinatario Correo del usuario.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void sendHtmlEmail(List<String> datos, String nombreHTML, String asunto, @NonNull String destinatario) 
            throws MessagingException, IOException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
        if (asunto != null) { helper.setSubject(asunto); }
        helper.setFrom(new InternetAddress(REMITENTE));
        helper.setTo(destinatario);
        
		Resource resource = new ClassPathResource("templates/email/" + nombreHTML);
        String htmlTemplate = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);

        // Contenido HTML del correo electrónico
        for (String string : datos) {
            String[] datosParaReemplazar = string.split(",");
            String variable = datosParaReemplazar[0].trim();
            String valor = datosParaReemplazar[1].trim();
            htmlTemplate = htmlTemplate.replace(variable, valor);
        }

        mensaje.setContent(htmlTemplate, "text/html; charset=utf-8");
        mailSender.send(mensaje);      
    }

}