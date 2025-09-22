package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RelatorioTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        String columnName = table.getColumnName(column);

        switch (columnName) {
            case "Status":
                configurarCelulaStatus(c, value);
                break;
            case "Progresso":
                configurarCelulaProgresso(c, value);
                break;
            case "Dias Restantes":
                configurarCelulaDiasRestantes(c, value);
                break;
            case "Taxa Conclusão":
                configurarCelulaTaxaConclusao(c, value);
                break;
            case "Produtividade":
                configurarCelulaProdutividade(c, value);
                break;
            case "Nível de Risco":
                configurarCelulaNivelRisco(c, value);
                break;
            case "Dias de Atraso":
                configurarCelulaDiasAtraso(c, value);
                break;
            default:
                configurarCelulaPadrao(c);
        }

        return c;
    }

    private void configurarCelulaStatus(Component c, Object value) {
        if (value instanceof String) {
            String status = (String) value;
            c.setFont(c.getFont().deriveFont(Font.BOLD));

            if (status.contains("✅") || status.contains("Concluído")) {
                c.setBackground(new Color(220, 255, 220)); // Verde claro
                c.setForeground(new Color(0, 100, 0));     // Verde escuro
            } else if (status.contains("🚀") || status.contains("Andamento")) {
                c.setBackground(new Color(220, 230, 255)); // Azul claro
                c.setForeground(new Color(0, 0, 150));     // Azul escuro
            } else if (status.contains("📋") || status.contains("Planejado")) {
                c.setBackground(new Color(255, 255, 200)); // Amarelo claro
                c.setForeground(new Color(150, 150, 0));   // Amarelo escuro
            } else if (status.contains("❌") || status.contains("Cancelado")) {
                c.setBackground(new Color(255, 220, 220)); // Vermelho claro
                c.setForeground(new Color(150, 0, 0));     // Vermelho escuro
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarCelulaProgresso(Component c, Object value) {
        if (value instanceof String) {
            String progressoStr = (String) value;
            try {
                double progresso = Double.parseDouble(progressoStr.replace("%", ""));

                if (progresso >= 90) {
                    c.setBackground(new Color(200, 255, 200)); // Verde
                    c.setForeground(new Color(0, 100, 0));
                } else if (progresso >= 70) {
                    c.setBackground(new Color(255, 255, 200)); // Amarelo
                    c.setForeground(new Color(150, 150, 0));
                } else if (progresso >= 50) {
                    c.setBackground(new Color(255, 220, 200)); // Laranja
                    c.setForeground(new Color(150, 75, 0));
                } else {
                    c.setBackground(new Color(255, 200, 200)); // Vermelho
                    c.setForeground(new Color(150, 0, 0));
                }

                c.setFont(c.getFont().deriveFont(Font.BOLD));

            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarCelulaDiasRestantes(Component c, Object value) {
        if (value instanceof String) {
            String diasStr = (String) value;

            if (diasStr.contains("Expirado")) {
                c.setBackground(new Color(255, 200, 200)); // Vermelho
                c.setForeground(new Color(150, 0, 0));
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else {
                try {
                    int dias = Integer.parseInt(diasStr.replace(" dias", ""));

                    if (dias <= 7) {
                        c.setBackground(new Color(255, 220, 200)); // Laranja
                        c.setForeground(new Color(150, 75, 0));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (dias <= 30) {
                        c.setBackground(new Color(255, 255, 200)); // Amarelo
                        c.setForeground(new Color(150, 150, 0));
                    } else {
                        c.setBackground(new Color(220, 255, 220)); // Verde
                        c.setForeground(new Color(0, 100, 0));
                    }

                } catch (NumberFormatException e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }
        }
    }

    private void configurarCelulaTaxaConclusao(Component c, Object value) {
        configurarCelulaProgresso(c, value); // Reutiliza a mesma lógica do progresso
    }

    private void configurarCelulaProdutividade(Component c, Object value) {
        if (value instanceof String) {
            String produtividade = (String) value;
            c.setFont(c.getFont().deriveFont(Font.BOLD));

            if (produtividade.contains("⭐")) {
                c.setBackground(new Color(220, 255, 220)); // Verde
                c.setForeground(new Color(0, 100, 0));
            } else if (produtividade.contains("👍")) {
                c.setBackground(new Color(255, 255, 200)); // Amarelo
                c.setForeground(new Color(150, 150, 0));
            } else if (produtividade.contains("⚠️")) {
                c.setBackground(new Color(255, 220, 200)); // Laranja
                c.setForeground(new Color(150, 75, 0));
            } else if (produtividade.contains("❌")) {
                c.setBackground(new Color(255, 200, 200)); // Vermelho
                c.setForeground(new Color(150, 0, 0));
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarCelulaNivelRisco(Component c, Object value) {
        if (value instanceof String) {
            String nivel = (String) value;
            c.setFont(c.getFont().deriveFont(Font.BOLD));

            if (nivel.contains("🔴") || nivel.contains("Crítico")) {
                c.setBackground(new Color(255, 200, 200)); // Vermelho
                c.setForeground(new Color(150, 0, 0));
            } else if (nivel.contains("🟡") || nivel.contains("Alto")) {
                c.setBackground(new Color(255, 220, 200)); // Laranja
                c.setForeground(new Color(150, 75, 0));
            } else if (nivel.contains("🟠") || nivel.contains("Médio")) {
                c.setBackground(new Color(255, 255, 200)); // Amarelo
                c.setForeground(new Color(150, 150, 0));
            } else if (nivel.contains("🔵") || nivel.contains("Baixo")) {
                c.setBackground(new Color(220, 255, 220)); // Verde
                c.setForeground(new Color(0, 100, 0));
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarCelulaDiasAtraso(Component c, Object value) {
        if (value instanceof String) {
            String diasStr = (String) value;

            try {
                int dias = Integer.parseInt(diasStr.replace(" dias", ""));

                if (dias > 30) {
                    c.setBackground(new Color(255, 200, 200)); // Vermelho
                    c.setForeground(new Color(150, 0, 0));
                } else if (dias > 15) {
                    c.setBackground(new Color(255, 220, 200)); // Laranja
                    c.setForeground(new Color(150, 75, 0));
                } else if (dias > 7) {
                    c.setBackground(new Color(255, 255, 200)); // Amarelo
                    c.setForeground(new Color(150, 150, 0));
                } else {
                    c.setBackground(new Color(220, 255, 220)); // Verde
                    c.setForeground(new Color(0, 100, 0));
                }

                c.setFont(c.getFont().deriveFont(Font.BOLD));

            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarCelulaPadrao(Component c) {
        c.setBackground(Color.WHITE);
        c.setForeground(Color.BLACK);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    @Override
    protected void setValue(Object value) {
        if (value instanceof String) {
            String text = (String) value;
            // Remove emojis para o texto base (mantém apenas para display visual)
            String cleanText = text.replaceAll("[📋🚀✅❌⭐👍⚠️🔴🟡🟠🔵]", "").trim();
            super.setValue(cleanText.isEmpty() ? text : cleanText);
        } else {
            super.setValue(value);
        }
    }
}