package dialogueUtilisateur;

import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


import src.Connect;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CandidatEquipes extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableInscrits;
	private JScrollPane scrollPane;
	private JTable tableEquipes;
	private JScrollPane scrollPane_1;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CandidatEquipes frame = new CandidatEquipes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	/**
	 * Create the frame.
	 */
	public CandidatEquipes(final Personne candidat, Inscriptions inscriptions) {
		final SortedSet<Equipe> equipes = inscriptions.getEquipes();
		final Set<Equipe> inscrits = candidat.getEquipes();
		
		setTitle("Equipes");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 670, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		final DefaultTableModel modelInscrits= new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas être éditées
		       }
		};
		final DefaultTableModel modelEquipes= new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas être éditées
		       }
		};
		scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 50, 230, 440);
		contentPane.add(scrollPane);
		
		tableInscrits = new JTable();
		
		
		Object [] columnsInscrits = {"id","nom"};
		modelInscrits.setColumnIdentifiers(columnsInscrits);
		tableInscrits.setModel(modelInscrits);
		scrollPane.setViewportView(tableInscrits);
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(410, 50, 230, 440);
		contentPane.add(scrollPane_1);
		
		tableEquipes = new JTable();
		
		Object [] columnsEquipes = {"id","nom"};
		modelEquipes.setColumnIdentifiers(columnsEquipes);
		tableEquipes.setModel(modelEquipes);
		scrollPane_1.setViewportView(tableEquipes);
		
		for (Iterator<Equipe> iterator = equipes.iterator(); iterator.hasNext();) {
			Equipe equipe = (Equipe) iterator.next();
			System.out.println(equipe.getNom());
		
			Object[] row = new Object[columnsEquipes.length];
			row[0] = equipe.getIdCandidat();
			row[1] = equipe.getNom();

			modelEquipes.addRow(row);
			
		} 
		
		for (Iterator<Equipe> iterator = inscrits.iterator(); iterator.hasNext();) {
			Equipe inscrit = (Equipe) iterator.next();
			
			System.out.println("lala");
			Object[] row = new Object[columnsInscrits.length];
			row[0] = inscrit.getIdCandidat();
			row[1] = inscrit.getNom();

			modelInscrits.addRow(row);
			
		} 
		JButton btnAjouterEquipe = new JButton("Ajouter");
		btnAjouterEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
					try{
						int i = tableEquipes.getSelectedRow();
						
						int idEquipeSelectionnee = (int)tableEquipes.getValueAt(i, 0);
						if(i >= 0){
							for (Iterator<Equipe> iterator = equipes.iterator(); iterator
									.hasNext();) {
								
								Equipe equipe = (Equipe) iterator.next();
								
								if(equipe.getIdCandidat() == idEquipeSelectionnee){
									
									inscrits.add(equipe);
									Connect connect = new Connect();
									try{
										connect.addMembreEquipe(equipe.getIdCandidat(), candidat.getIdCandidat());
										Object[] row = new Object[4];
										row[0] = equipe.getIdCandidat();
										row[1] = equipe.getNom();
										modelInscrits.addRow(row);
									}
									catch(SQLException e){
										
										if (e instanceof SQLIntegrityConstraintViolationException) {
											JOptionPane.showMessageDialog(null, "Déjà membre");
									    } 
										else
											e.printStackTrace();
									}
									connect.close();
									

								}	
							}
		
						}
					}
					catch (Exception e) {

						if(e instanceof java.lang.ArrayIndexOutOfBoundsException)
							JOptionPane.showMessageDialog(null, "Selectionner une equipe");
						else
							e.printStackTrace();
					}
				}
			
		});
		btnAjouterEquipe.setBounds(280, 90, 115, 30);
		contentPane.add(btnAjouterEquipe);
		
		JButton btnSupprimerEquipe = new JButton("Supprimer");
		btnSupprimerEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tableInscrits.getSelectedRow();
					
					int idEquipeSelectionnee = (int)tableInscrits.getValueAt(i, 0);
					if(i >= 0){
						for (Iterator<Equipe> iterator = inscrits.iterator(); iterator
								.hasNext();) {
							
							Equipe inscrit = (Equipe) iterator.next();
							
							if(inscrit.getIdCandidat() == idEquipeSelectionnee){
								
								Connect connect = new Connect();
								connect.delMembreEquipe(inscrit.getIdCandidat(), candidat.getIdCandidat());
								connect.close();

								modelInscrits.removeRow(i);
								iterator.remove();
								//membres.remove(membre);
								 
								
							}	
						}
	
					}
				}catch (Exception e) {

					if(e instanceof java.lang.ArrayIndexOutOfBoundsException)
						JOptionPane.showMessageDialog(null, "Selectionner un inscrit");
					else
						e.printStackTrace();
				}
					
			}
		});
		btnSupprimerEquipe.setBounds(280, 130, 115, 30);
		contentPane.add(btnSupprimerEquipe);
		
		JLabel lblInscrits = new JLabel("Equipes du candidats");
		lblInscrits.setHorizontalAlignment(SwingConstants.CENTER);
		lblInscrits.setBounds(85, 20, 130, 25);
		contentPane.add(lblInscrits);
		
		lblNewLabel = new JLabel("Equipes");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(500, 20, 50, 20);
		contentPane.add(lblNewLabel);
	}
}
