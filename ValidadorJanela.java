import java.awt.*;
import javax.swing.*;

public class ValidadorJanela extends JFrame {

    private static final Color VERDE = new Color(0, 140, 0);
    private static final Color VERMELHO = new Color(200, 0, 0);

    public ValidadorJanela() {
        super("Validador de Documentos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("CPF", criarPainel(true));
        abas.addTab("CNPJ", criarPainel(false));
        add(abas);

        setSize(440, 230);
        setLocationRelativeTo(null); // centraliza na tela
    }

    private JPanel criarPainel(boolean ehCpf) {
        String nome = ehCpf ? "CPF" : "CNPJ";

        JTextField campo = new JTextField(20);
        JLabel resultado = new JLabel(" ", SwingConstants.CENTER);
        resultado.setFont(resultado.getFont().deriveFont(Font.BOLD, 16f));

        JButton validar = new JButton("Validar");
        JButton gerar = new JButton("Gerar para teste");
        JButton limpar = new JButton("Limpar");

        // Acao de validar (usada pelo botao e pela tecla Enter)
        Runnable validarAcao = () -> {
            String texto = campo.getText().trim();
            if (texto.isEmpty()) {
                mostrar(resultado, "Digite um " + nome, Color.GRAY);
                return;
            }
            boolean ok = ehCpf ? Validador.isCpfValido(texto) : Validador.isCnpjValido(texto);
            if (ok) {
                mostrar(resultado, nome + " VALIDO", VERDE);
            } else {
                mostrar(resultado, nome + " INVALIDO", VERMELHO);
            }
        };

        validar.addActionListener(e -> validarAcao.run());
        campo.addActionListener(e -> validarAcao.run());

        gerar.addActionListener(e -> {
            campo.setText(ehCpf
                    ? Validador.formatarCpf(Validador.gerarCpf())
                    : Validador.formatarCnpj(Validador.gerarCnpj()));
            resultado.setText(" ");
        });

        limpar.addActionListener(e -> {
            campo.setText("");
            resultado.setText(" ");
            campo.requestFocusInWindow();
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        topo.add(new JLabel(nome + ":"));
        topo.add(campo);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        botoes.add(validar);
        botoes.add(gerar);
        botoes.add(limpar);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        painel.add(topo, BorderLayout.NORTH);
        painel.add(resultado, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);
        return painel;
    }

    private void mostrar(JLabel label, String texto, Color cor) {
        label.setText(texto);
        label.setForeground(cor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ValidadorJanela().setVisible(true));
    }
}