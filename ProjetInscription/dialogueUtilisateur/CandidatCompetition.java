package dialogueUtilisateur;


import inscriptions.Competition;
import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;



import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import src.Connect;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JScrollPane;


import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class CandidatCompetition extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableInscrits;
	private JTable tableCandidats;

	/**
	 * Create the frame.
	 */
	public CandidatCompetition(final Competition competition, Inscriptions inscription) {
		setTitle("Inscriptions � une comp�tition");
		final DefaultTableModel modelInscrits= new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas �tre �dit�es
		       }
		};
		final DefaultTableModel modelCandidats= new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas �tre �dit�es
		       }
		};
		setResizable(false);
		
		final SortedSet<Personne> personnes = inscription.getPersonnes();
		final SortedSet<Equipe> equipes = inscription.getEquipes();
		Connect connect = new Connect();
		SortedSet<Personne> candidatsInscrits = connect.getCandidatsInscrits(competition);
		connect.close();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(44, 44, 300, 385);
		contentPane.add(scrollPane);
		
		tableInscrits = new JTable();
		tableInscrits.setModel(modelInscrits);

		if(!competition.estEnEquipe()){
			System.out.println("Row ok");
			Object [] columnsInscrits = {"id","nom","prenom","nom"};
			modelInscrits.setColumnIdentifiers(columnsInscrits);
		}
		else{
			Object [] columnsInscrits = {"id","nom"};
			modelInscrits.setColumnIdentifiers(columnsInscrits);
		}
		for (Iterator<Personne> iterator = candidatsInscrits.iterator(); iterator.hasNext();) {
			Personne candidatInscrit = (Personne) iterator.next();
			System.out.println(candidatInscrit.getNom());
			if(!competition.estEnEquipe()){
				System.out.println("Model ok");
				
				Object[] row = new Object[4];
				row[0] = candidatInscrit.getIdCandidat();
				row[1] = candidatInscrit.getNom();
				row[2] = candidatInscrit.getPrenom();
				row[3] = candidatInscrit.getMail();
				modelInscrits.addRow(row);
			}
			else{
				
				
				Object[] row = new Object[2];
				row[0] = candidatInscrit.getIdCandidat();
				row[1] = candidatInscrit.getNom();
				modelInscrits.addRow(row);
			}
		}
		
		scrollPane.setViewportView(tableInscrits);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(503, 44, 296, 386);
		contentPane.add(scrollPane_2);
		
		tableCandidats = new JTable();
		scrollPane_2.setViewportView(tableCandidats);
		
		if(!competition.estEnEquipe()){
			
			Object [] columnsCandidats = {"id","nom","prenom","mail"};
			modelCandidats.setColumnIdentifiers(columnsCandidats);
		}
		else{
			Object [] columnsCandidats = {"id","nom"};
			modelCandidats.setColumnIdentifiers(columnsCandidats);
		}
		if(!competition.estEnEquipe()){
			for (Iterator<Personne> iterator = personnes.iterator(); iterator.hasNext();) {
				Personne personne = (Personne) iterator.next();
			
			
				System.out.println("Model ok");
				tableCandidats.setModel(modelCandidats);
				Object[] row = new Object[4];
				row[0] = personne.getIdCandidat();
				row[1] = personne.getNom();
				row[2] = personne.getPrenom();
				row[3] = personne.getMail();
				modelCandidats.addRow(row);
			}
		}
		else{
			for (Iterator<Equipe> iterator = equipes.iterator(); iterator.hasNext();) {
				Equipe equipe = (Equipe) iterator.next();
				tableCandidats.setModel(modelCandidats);
				Object[] row = new Object[2];
				row[0] = equipe.getIdCandidat();
				row[1] = equipe.getNom();
				modelCandidats.addRow(row);
			}
			
		}
		tableCandidats.setModel(modelCandidats);
		JButton btnInscrire = new JButton("Inscrire");
		btnInscrire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tableCandidats.getSelectedRow();
					int idCandidatSelectionnee = (int)tableCandidats.getValueAt(i, 0);
					if(!competition.estEnEquipe()){
						for (Iterator<Personne> iterator = personnes.iterator(); iterator
								.hasNext();) {
							
							Personne personne = (Personne) iterator.next();
							
							if(personne.getIdCandidat() == idCandidatSelectionnee){
								
								Connect connect = new Connect();
								connect.addParticipation(personne.getIdCandidat(), competition.getId());
								connect.close();
								Object[] row = new Object[4];
								row[0] = personne.getIdCandidat();
								row[1] = personne.getNom();
								row[2] = personne.getPrenom();
								row[3] = personne.getMail();								 
								modelInscrits.addRow(row);
							}	
						}
					}else{
						for (Iterator<Equipe> iterator = equipes.iterator(); iterator
								.hasNext();) {
							
							Equipe equipe = (Equipe) iterator.next();
							
							if(equipe.getIdCandidat() == idCandidatSelectionnee){
							
								Connect connect = new Connect();
								connect.addParticipation(equipe.getIdCandidat(), competition.getId());
								connect.close();
								Object[] row = new Object[2];
								row[0] = equipe.getIdCandidat();
								row[1] = equipe.getNom();
								modelInscrits.addRow(row);
								
							}	
						}
					}
				}
				catch  (Exception e){
					System.out.println(e);
					if (e instanceof SQLIntegrityConstraintViolationException) {
						JOptionPane.showMessageDialog(null, "D�j� inscrit");
				    }
					if( e instanceof ArrayIndexOutOfBoundsException)
						JOptionPane.showMessageDialog(null, "Selectionner un candidat � ajouter ");
				
				    
				}
				
			}
		});
		btnInscrire.setBounds(366, 99, 114, 23);
		contentPane.add(btnInscrire);
		
		JButton btnDesincrire = new JButton("D�sinscrire");
		btnDesincrire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tableInscrits.getSelectedRow();
					int idCandidatSelectionnee = (int)tableInscrits.getValueAt(i, 0);
					
					for (Iterator<Personne> iterator = personnes.iterator(); iterator
						.hasNext();) {
							
						Personne personne = (Personne) iterator.next();
							
						if(personne.getIdCandidat() == idCandidatSelectionnee){
							
							Connect connect = new Connect();
							connect.delParticipation(personne.getIdCandidat(), competition.getId());
							connect.close();
							
							modelInscrits.removeRow(i);
						}	
					}
					for (Iterator<Equipe> iterator = equipes.iterator(); iterator
							.hasNext();) {
								
							Equipe equipe = (Equipe) iterator.next();
								
							if(equipe.getIdCandidat() == idCandidatSelectionnee){
								
								Connect connect = new Connect();
								connect.delParticipation(equipe.getIdCandidat(), competition.getId());
								connect.close();
									 
								modelInscrits.removeRow(i);
							}	
						}
				}
				catch  (Exception e){
					System.out.println(e);
					if( e instanceof ArrayIndexOutOfBoundsException)
						JOptionPane.showMessageDialog(null, "Selectionner un candidat � d�sinscrire ");
				
				    
				}
			}
		});
		btnDesincrire.setBounds(366, 153, 114, 23);
		contentPane.add(btnDesincrire);
		
		JLabel lblNewLabel = new JLabel("Inscrits");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(160, 19, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Candidats");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(586, 19, 132, 14);
		contentPane.add(lblNewLabel_1);
	}
}
