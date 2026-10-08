import java.util.Random;
import java.util.Scanner;

public class Validador {

    // ---------- CPF ----------

    public static boolean isCpfValido(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;

        int d1 = calcularDigito(cpf.substring(0, 9), 10);
        int d2 = calcularDigito(cpf.substring(0, 9) + d1, 11);
        return cpf.equals(cpf.substring(0, 9) + d1 + d2);
    }

    public static String gerarCpf() {
        Random r = new Random();
        StringBuilder base = new StringBuilder();
        for (int i = 0; i < 9; i++) base.append(r.nextInt(10));

        // evita sequencias repetidas (ex: 000000000)
        if (base.toString().matches("(\\d)\\1{8}")) return gerarCpf();

        int d1 = calcularDigito(base.toString(), 10);
        int d2 = calcularDigito(base.toString() + d1, 11);
        return base.toString() + d1 + d2;
    }

    public static String formatarCpf(String cpf) {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    // ---------- CNPJ ----------

    private static final int[] PESOS_CNPJ_1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CNPJ_2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null) return false;
        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14) return false;
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        int d1 = calcularDigitoCnpj(cnpj.substring(0, 12), PESOS_CNPJ_1);
        int d2 = calcularDigitoCnpj(cnpj.substring(0, 12) + d1, PESOS_CNPJ_2);
        return cnpj.equals(cnpj.substring(0, 12) + d1 + d2);
    }

    public static String gerarCnpj() {
        Random r = new Random();
        StringBuilder base = new StringBuilder();
        for (int i = 0; i < 8; i++) base.append(r.nextInt(10));
        base.append("0001"); // numero de filial padrao

        int d1 = calcularDigitoCnpj(base.toString(), PESOS_CNPJ_1);
        int d2 = calcularDigitoCnpj(base.toString() + d1, PESOS_CNPJ_2);
        return base.toString() + d1 + d2;
    }

    public static String formatarCnpj(String cnpj) {
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "."
                + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
    }

    // ---------- Calculos ----------

    private static int calcularDigito(String base, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;
        for (char c : base.toCharArray()) {
            soma += Character.getNumericValue(c) * peso;
            peso--;
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    private static int calcularDigitoCnpj(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // ---------- Menu ----------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean rodando = true;

        while (rodando) {
            System.out.println("\n=== Validador de Documentos ===");
            System.out.println("1 - Validar CPF");
            System.out.println("2 - Validar CNPJ");
            System.out.println("3 - Gerar CPF valido (para testes)");
            System.out.println("4 - Gerar CNPJ valido (para testes)");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            String opcao = sc.nextLine().trim();

            switch (opcao) {
                case "1":
                    System.out.print("Digite o CPF: ");
                    String cpf = sc.nextLine();
                    System.out.println(isCpfValido(cpf) ? "CPF VALIDO" : "CPF INVALIDO");
                    break;
                case "2":
                    System.out.print("Digite o CNPJ: ");
                    String cnpj = sc.nextLine();
                    System.out.println(isCnpjValido(cnpj) ? "CNPJ VALIDO" : "CNPJ INVALIDO");
                    break;
                case "3":
                    System.out.println("CPF gerado: " + formatarCpf(gerarCpf()));
                    break;
                case "4":
                    System.out.println("CNPJ gerado: " + formatarCnpj(gerarCnpj()));
                    break;
                case "0":
                    rodando = false;
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }

        sc.close();
        System.out.println("Encerrado.");
    }
}