package test10_b;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Test10_b extends JFrame {
    private JTextField txtNim, txtNama;
    private JButton btnSimpan, btnEdit, btnHapus, btnCari, btnKeluar;
    private JComboBox<String> comboMatakuliah;
    private JRadioButton radioA, radioB, radioC;
    private ButtonGroup kelasGroup;
    private JTextArea hasilArea;
    private Connection conn;
    private Statement stmt;

    public Test10_b() {
        // Set up the GUI components
        setTitle("My GUI");
        setLayout(null);

        JLabel lblNim = new JLabel("NIM:");
        lblNim.setBounds(20, 20, 80, 25);
        add(lblNim);

        txtNim = new JTextField();
        txtNim.setBounds(110, 20, 150, 25);
        add(txtNim);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 60, 80, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(110, 60, 150, 25);
        add(txtNama);

        JLabel lblKelas = new JLabel("Kelas:");
        lblKelas.setBounds(20, 100, 80, 25);
        add(lblKelas);

        radioA = new JRadioButton("A");
        radioA.setBounds(110, 100, 50, 25);
        add(radioA);

        radioB = new JRadioButton("B");
        radioB.setBounds(160, 100, 50, 25);
        add(radioB);

        radioC = new JRadioButton("C");
        radioC.setBounds(210, 100, 50, 25);
        add(radioC);

        kelasGroup = new ButtonGroup();
        kelasGroup.add(radioA);
        kelasGroup.add(radioB);
        kelasGroup.add(radioC);

        JLabel lblMatakuliah = new JLabel("Matakuliah:");
        lblMatakuliah.setBounds(20, 140, 80, 25);
        add(lblMatakuliah);

        String[] matakuliah = {"Kalkulus", "Matematika", "Fisika", "Kimia", "Biologi", "Ethical Hacking"};
        comboMatakuliah = new JComboBox<>(matakuliah);
        comboMatakuliah.setBounds(110, 140, 150, 25);
        add(comboMatakuliah);

        btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 180, 80, 30);
        add(btnSimpan);

        btnEdit = new JButton("Edit");
        btnEdit.setBounds(110, 180, 80, 30);
        add(btnEdit);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(200, 180, 80, 30);
        add(btnHapus);

        btnCari = new JButton("Cari");
        btnCari.setBounds(20, 220, 80, 30);
        add(btnCari);

        btnKeluar = new JButton("Keluar");
        btnKeluar.setBounds(110, 220, 80, 30);
        add(btnKeluar);

        hasilArea = new JTextArea();
        hasilArea.setBounds(300, 20, 300, 230);
        hasilArea.setEditable(false); // Membuat JTextArea menjadi non-editable
        add(hasilArea);

        // Set up database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/modul10b", "root", "");
            stmt = conn.createStatement();
            System.err.println("Terkoneksi, OK!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal Terkonek, Yah...");
            System.exit(1);
        }

        // Add action listeners for buttons
        btnSimpan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ambil data dari input pengguna
                String nim = txtNim.getText();
                String nama = txtNama.getText();
                String kelas = "";
                if (radioA.isSelected()) {
                    kelas = "A";
                } else if (radioB.isSelected()) {
                    kelas = "B";
                } else if (radioC.isSelected()) {
                    kelas = "C";
                }
                String matakuliah = (String) comboMatakuliah.getSelectedItem();

                // Query SQL untuk menyimpan data ke dalam database
                String insertQuery = "INSERT INTO mahasiswa (nim, nama, kelas, matakuliah) VALUES (?, ?, ?, ?)";

                try {
                    // Persiapkan statement SQL
                    PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                    preparedStatement.setString(1, nim);
                    preparedStatement.setString(2, nama);
                    preparedStatement.setString(3, kelas);
                    preparedStatement.setString(4, matakuliah);

                    // Eksekusi pernyataan SQL
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal menyimpan data.");
                    }

                    // Reset inputan setelah penyimpanan berhasil
                    txtNim.setText("");
                    txtNama.setText("");
                    kelasGroup.clearSelection();
                    comboMatakuliah.setSelectedIndex(0);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan data.");
                }
            }
        });



        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ambil NIM dari input pengguna
                String nimToEdit = txtNim.getText();

                // Query SQL untuk mengupdate data berdasarkan NIM
                String updateQuery = "UPDATE mahasiswa SET nama=?, kelas=?, matakuliah=? WHERE nim=?";

                try {
                    // Persiapkan statement SQL
                    PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
                    preparedStatement.setString(1, txtNama.getText());

                    String kelas = "";
                    if (radioA.isSelected()) {
                        kelas = "A";
                    } else if (radioB.isSelected()) {
                        kelas = "B";
                    } else if (radioC.isSelected()) {
                        kelas = "C";
                    }
                    preparedStatement.setString(2, kelas);

                    preparedStatement.setString(3, (String) comboMatakuliah.getSelectedItem());
                    preparedStatement.setString(4, nimToEdit);

                    // Eksekusi pernyataan SQL
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Data dengan NIM " + nimToEdit + " berhasil diubah.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Data dengan NIM " + nimToEdit + " tidak ditemukan atau gagal diubah.");
                    }
                    
                    // Reset inputan setelah penyimpanan berhasil
                    txtNim.setText("");
                    txtNama.setText("");
                    kelasGroup.clearSelection();
                    comboMatakuliah.setSelectedIndex(0);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengedit data.");
                }
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ambil NIM dari input pengguna
                String nimToDelete = txtNim.getText();

                // Query SQL untuk menghapus data berdasarkan NIM
                String deleteQuery = "DELETE FROM mahasiswa WHERE nim=?";

                try {
                    // Persiapkan statement SQL
                    PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
                    preparedStatement.setString(1, nimToDelete);

                    // Eksekusi pernyataan SQL
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Data dengan NIM " + nimToDelete + " berhasil dihapus.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Data dengan NIM " + nimToDelete + " tidak ditemukan atau gagal dihapus.");
                    }

                    // Reset inputan setelah penghapusan berhasil
                    txtNim.setText("");
                    txtNama.setText("");
                    kelasGroup.clearSelection();
                    comboMatakuliah.setSelectedIndex(0);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data.");
                }
            }
        });


        btnCari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ambil NIM dari input pengguna
                String nimToSearch = txtNim.getText();

                // Query SQL untuk mencari data berdasarkan NIM
                String selectQuery = "SELECT * FROM mahasiswa WHERE nim=?";

                try {
                    // Persiapkan statement SQL
                    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
                    preparedStatement.setString(1, nimToSearch);

                    // Eksekusi pernyataan SQL dan ambil hasil
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Tampilkan hasil pencarian di JTextArea
                    if (resultSet.next()) {
                        String nama = resultSet.getString("nama");
                        String kelas = resultSet.getString("kelas");
                        String matakuliah = resultSet.getString("matakuliah");

                        String hasilPencarian = "NIM: " + nimToSearch + "\n";
                        hasilPencarian += "Nama: " + nama + "\n";
                        hasilPencarian += "Kelas: " + kelas + "\n";
                        hasilPencarian += "Matakuliah: " + matakuliah;

                        hasilArea.setText(hasilPencarian);
                    } else {
                        JOptionPane.showMessageDialog(null, "Data dengan NIM " + nimToSearch + " tidak ditemukan/cari menggunakan NIM.");
                        hasilArea.setText(""); // Menghapus hasil pencarian sebelumnya
                    }
                    
                    // Reset inputan setelah penyimpanan berhasil
                    txtNim.setText("");
                    txtNama.setText("");
                    kelasGroup.clearSelection();
                    comboMatakuliah.setSelectedIndex(0);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mencari data.");
                }
            }
        });

        
        btnKeluar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    // Tambahkan konfirmasi sebelum keluar
                    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Tutup koneksi database jika masih terbuka
                        try {
                            if (stmt != null) {
                                stmt.close();
                            }
                            if (conn != null) {
                                conn.close();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        // Keluar dari aplikasi
                        System.exit(0);
                    }
            }
        });


        setSize(640, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Test10_b();
            }
        });
    }
}