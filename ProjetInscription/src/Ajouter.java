package src;
import inscriptions.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.sun.xml.internal.ws.api.message.Message;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTable;

public class Ajouter extends JFrame {
	final int maxLength = 25;
	private JPanel contentPane;
	private JTextField nomEquipe;
	private Inscriptions inscriptions = new Inscriptions();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ajouter frame = new Ajouter();
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
	public Ajouter() {
		setTitle("Ajout d'une équipe");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 390, 201);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nomEquipe = new JTextField();
		nomEquipe.setBounds(100, 53, 164, 20);
		contentPane.add(nomEquipe);
		nomEquipe.setColumns(10);
		
		JButton btnNewButton = new JButton("Ajouter");
		btnNewButton.setBounds(121, 96, 118, 20);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(nomEquipe.getText()!= null && nomEquipe.getText().length()<=maxLength){
					inscriptions.createEquipe(nomEquipe.getText());
					dispose();
					inscriptions.closeConnection();
				}
				else{
					nomEquipe.setText(null);
					JOptionPane jop1 = new JOptionPane();
					JOptionPane.showMessageDialog(null, "Le nom doit faire entre 1 et 25 carac.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		contentPane.add(btnNewButton);
		
		JLabel lblNomEquipe = new JLabel("Nom Equipe");
		lblNomEquipe.setBounds(151, 27, 93, 14);
		contentPane.add(lblNomEquipe);
		Object [] columns = {"id","nom"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
	}
}
