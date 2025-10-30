package view;
import dao.Conexao;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class PainelVan extends JPanel{
    private JTextField tfPlaca = new JTextField(10);
    private JTextField tfModelo = new JTextField(20);
    private JSpinner spCapacidade = new JSpinner(new SpinnerNumberModel(15, 1, 100, 1));
    private JComboBox<String> cbCondicao = new JComboBox<>(new String[]{"ATIVA", "MANUTENÇÃO", "INATIVA"});
    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnEditar = new JButton("Editar");
    private JButton btnExcluir = new JButton("Excluir");
    private JButton btnAtualizar = new JButton("Atualizar");
    private JTable tabela;
    private DefaultTableModel modelo;

    private int idSelecionado = -1;

    public PainelVan() {
        setLayout(null);

        JLabel lbPlaca = new JLabel("Placa:");
        JLabel lbModelo = new JLabel("Modelo:");
        JLabel lbCapacidade = new JLabel("Capacidade:");
        JLabel lbCondicao = new JLabel("Condição:");

        lbPlaca.setBounds(30, 20, 100, 25);
        tfPlaca.setBounds(140, 20, 200, 25);
        lbModelo.setBounds(30, 60, 100, 25);
        tfModelo.setBounds(140, 60, 200, 25);
        lbCapacidade.setBounds(30, 100, 100, 25);
        spCapacidade.setBounds(140, 100, 60, 25);
        lbCondicao.setBounds(30, 140, 100, 25);
        cbCondicao.setBounds(140, 140, 200, 25);

        btnSalvar.setBounds(30, 180, 90, 25);
        btnEditar.setBounds(130, 180, 90, 25);
        btnExcluir.setBounds(230, 180, 90, 25);
        btnAtualizar.setBounds(330, 180, 110, 25);

        add(lbPlaca); add(tfPlaca);
        add(lbModelo); add(tfModelo);
        add(lbCapacidade); add(spCapacidade);
        add(lbCondicao); add(cbCondicao);
        add(btnSalvar); add(btnEditar); add(btnExcluir); add(btnAtualizar);

        modelo = new DefaultTableModel(new Object[]{"ID", "Placa", "Modelo", "Capacidade", "Condição"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(30, 220, 520, 120);
        add(scroll);

        btnSalvar.addActionListener(e -> salvarVan());
        btnEditar.addActionListener(e -> editarVan());
        btnExcluir.addActionListener(e -> excluirVan());
        btnAtualizar.addActionListener(e -> carregarTabela());

        carregarTabela();
    }

    private void salvarVan() {
        try (Connection conn = Conexao.getConecction()) {
            String sql;
            if (idSelecionado == -1) {
                sql = "INSERT INTO vans (placa, modelo, capacidade, condicao) VALUES (?, ?, ?, ?)";
            } else {
                sql = "UPDATE vans SET placa=?, modelo=?, capacidade=?, condicao=? WHERE id=?";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tfPlaca.getText());
            stmt.setString(2, tfModelo.getText());
            stmt.setInt(3, (Integer) spCapacidade.getValue());
            stmt.setString(4, cbCondicao.getSelectedItem().toString());
            if (idSelecionado != -1) stmt.setInt(5, idSelecionado);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Van salva com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void editarVan() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            idSelecionado = (int) modelo.getValueAt(linha, 0);
            tfPlaca.setText((String) modelo.getValueAt(linha, 1));
            tfModelo.setText((String) modelo.getValueAt(linha, 2));
            spCapacidade.setValue((int) modelo.getValueAt(linha, 3));
            cbCondicao.setSelectedItem(modelo.getValueAt(linha, 4));
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma van para editar.");
        }
    }

    private void excluirVan() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modelo.getValueAt(linha, 0);
            try (Connection conn = Conexao.getConecction()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM vans WHERE id=?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Van excluída!");
                carregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
            }
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try (Connection conn = Conexao.getConecction()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM vans ORDER BY id DESC");
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("capacidade"),
                        rs.getString("condicao")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage());
        }
    }

    private void limparCampos() {
        tfPlaca.setText("");
        tfModelo.setText("");
        spCapacidade.setValue(15);
        cbCondicao.setSelectedIndex(0);
        idSelecionado = -1;
    }
}
