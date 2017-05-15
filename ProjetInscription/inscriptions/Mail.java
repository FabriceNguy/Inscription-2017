package inscriptions;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Choice;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
public class Mail extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3148476096348832276L;
	private JPanel contentPane;
	private JTextField textSujet;
	private final JLabel lblSujet = new JLabel("Sujet :");
	private JLabel lblA;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mail frame = new Mail();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Mail() {
		
		final Inscriptions inscriptions = new Inscriptions();
		final SortedSet<Personne> candidats = inscriptions.getPersonnes();
		setTitle("Contacter un candidat");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 610, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		textSujet = new JTextField();
		textSujet.setBounds(90, 45, 450, 20);
		textSujet.setToolTipText("");
		contentPane.add(textSujet);
		textSujet.setColumns(10);
		lblSujet.setBounds(20, 45, 70, 20);
		contentPane.add(lblSujet);
		
		JLabel lblNewLabel = new JLabel("Message :");
		lblNewLabel.setBounds(20, 75, 70, 20);
		contentPane.add(lblNewLabel);
		
		final JTextArea textMessage = new JTextArea();
		textMessage.setBounds(90, 75, 450, 210);
		contentPane.add(textMessage);
		
		lblA = new JLabel("A :");
		lblA.setBounds(20, 15, 70, 20);
		contentPane.add(lblA);
		
		
		final Choice choiceAdressMail = new Choice();
		choiceAdressMail.setBounds(90, 15, 450, 20);
		contentPane.add(choiceAdressMail);
		//Recupere les adresses mails des candidats
		for (Iterator<Personne> iterator = candidats.iterator(); iterator.hasNext();) {
			Personne candidat = (Personne) iterator.next();
			
				choiceAdressMail.add(candidat.getMail());
		}
		
		JButton btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
		            String host ="smtp.gmail.com" ;
		            String user = "sio2e4@gmail.com";
		            String pass = "azertyuiop123";
		            String to = choiceAdressMail.getSelectedItem();
		            String from = "sio2e4@gmail.com";
		            String subject = textSujet.getText();
		            String messageText = textMessage.getText();
		            boolean sessionDebug = false;
	

		           
		            Properties props = System.getProperties();

		            props.put("mail.smtp.starttls.enable", "true");
		            props.put("mail.smtp.host", host);
		            props.put("mail.smtp.port", "587");
		            props.put("mail.smtp.auth", "true");
		            props.put("mail.smtp.starttls.required", "true");

		            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		            Session mailSession = Session.getDefaultInstance(props, null);
		            mailSession.setDebug(sessionDebug);
		            Message msg = new MimeMessage(mailSession);
		            msg.setFrom(new InternetAddress(from));
		            InternetAddress[] address = {new InternetAddress(to)};
		            msg.setRecipients(Message.RecipientType.TO, address);
		            msg.setSubject(subject);
		            msg.setSentDate(new Date());
		            msg.setContent("<p>"+messageText+"</p>","text/html; charset=utf-8");

		           Transport transport=mailSession.getTransport("smtp");
		           transport.connect(host, user, pass);
		           transport.sendMessage(msg, msg.getAllRecipients());
		           transport.close();
		           System.out.println("message send successfully");
		        }catch(Exception e)
		        {
		        	e.printStackTrace();
		        }
			}
		});
		btnEnvoyer.setBounds(90, 305, 450, 20);
		contentPane.add(btnEnvoyer);
		

		

    }
}


