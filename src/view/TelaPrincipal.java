package view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame{
    public TelaPrincipal() {
        setTitle("SmartVan - Sistema de Controle de Frotas");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel com abas
        JTabbedPane abas = new JTabbedPane();

        // Adiciona as duas abas principais
        abas.add("Cadastro de Vans", new PainelVan());
        abas.add("Cadastro de Motoristas", new PainelMotorista());

        add(abas);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}
