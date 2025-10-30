package view;
import dao.Conexao;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class PainelMotorista extends JPanel{
    private JTextField tfNome = new JTextField(20);
    private JTextField tfCNH = new JTextField(15);
    private JTextField tfTelefone = new JTextField(15);
    private JComboBox<String> cbVan = new JComboBox<>();
    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnEditar = new JButton("Editar");
    private JButton btnExcluir = new JButton("Excluir");
    private JButton btnAtualizar = new JButton("Atualizar");

    private JTable tabela;
    private DefaultTableModel modelo;
    private int idSelecionado = -1;

    public PainelMotorista() {
        setLayout(null);

        JLabel lbNome = new JLabel("Nome:");
        JLabel lbCNH = new JLabel("CNH:");
        JLabel lbTelefone = new JLabel("Telefone:");
        JLabel lbVan = new JLabel("Van associada:");

        lbNome.setBounds(30, 20, 100, 25);
        tfNome.setBounds(140, 20, 200, 25);
        lbCNH.setBounds(30, 60, 100, 25);
        tfCNH.setBounds(140, 60, 200, 25);
        lbTelefone.setBounds(30, 100, 100, 25);
        tfTelefone.setBounds(140, 100, 200, 25);
        lbVan.setBounds(30, 140, 100, 25);
        cbVan.setBounds(140, 140, 200, 25);

        btnSalvar.setBounds(30, 180, 90, 25);
        btnEditar.setBounds(130, 180, 90, 25);
        btnExcluir.setBounds(230, 180, 90, 25);
        btnAtualizar.setBounds(330, 180, 110, 25);

        add(lbNome); add(tfNome);
        add(lbCNH); add(tfCNH);
        add(lbTelefone); add(tfTelefone);
        add(lbVan); add(cbVan);
        add(btnSalvar); add(btnEditar); add(btnExcluir); add(btnAtualizar);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "CNH", "Telefone", "Van Associada"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 220, 520, 120);
        add(scroll);

        btnSalvar.addActionListener(e -> salvarMotorista());
        btnEditar.addActionListener(e -> editarMotorista());
        btnExcluir.addActionListener(e -> excluirMotorista());
        btnAtualizar.addActionListener(e -> {
            carregarVans();
            carregarTabela();
        });

        carregarVans();
        carregarTabela();
    }

    private void carregarVans() {
        cbVan.removeAllItems();
        cbVan.addItem("Sem Van");
        try (Connection conn = Conexao.getConecction()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, placa, modelo FROM vans");
            while (rs.next()) {
                cbVan.addItem(rs.getInt("id") + " - " + rs.getString("placa") + " (" + rs.getString("modelo") + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vans: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try (Connection conn = Conexao.getConecction()) {
            String sql = """
                    SELECT m.id, m.nome, m.cnh, m.telefone,
                           IFNULL(CONCAT(v.placa, ' - ', v.modelo), 'Sem Van') AS van_nome
                    FROM motorista m
                    LEFT JOIN vans v ON m.van_id = v.id
                    ORDER BY m.id DESC
                    """;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getString("telefone"),
                        rs.getString("van_nome")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar motorista: " + e.getMessage());
        }
    }

    private void salvarMotorista() {
        try (Connection conn = Conexao.getConecction()) {
            String sql;
            if (idSelecionado == -1) {
                sql = "INSERT INTO motorista (nome, cnh, telefone, van_id) VALUES (?, ?, ?, ?)";
            } else {
                sql = "UPDATE motorista SET nome=?, cnh=?, telefone=?, van_id=? WHERE id=?";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tfNome.getText());
            stmt.setString(2, tfCNH.getText());
            stmt.setString(3, tfTelefone.getText());

            String selecionado = (String) cbVan.getSelectedItem();
            if (selecionado == null || selecionado.equals("Sem Van")) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(4, Integer.parseInt(selecionado.split(" - ")[0]));
            }

            if (idSelecionado != -1) stmt.setInt(5, idSelecionado);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Motorista salvo com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void editarMotorista() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            idSelecionado = (int) modelo.getValueAt(linha, 0);
            tfNome.setText((String) modelo.getValueAt(linha, 1));
            tfCNH.setText((String) modelo.getValueAt(linha, 2));
            tfTelefone.setText((String) modelo.getValueAt(linha, 3));
            String vanAtual = (String) modelo.getValueAt(linha, 4);
            cbVan.setSelectedItem(vanAtual);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um motorista para editar.");
        }
    }

    private void excluirMotorista() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modelo.getValueAt(linha, 0);
            try (Connection conn = Conexao.getConecction()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM motorista WHERE id=?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Motorista excluído!");
                carregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        tfNome.setText("");
        tfCNH.setText("");
        tfTelefone.setText("");
        cbVan.setSelectedIndex(0);
        idSelecionado = -1;
    }
}