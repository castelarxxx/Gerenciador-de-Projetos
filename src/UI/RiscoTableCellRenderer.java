package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RiscoTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        c.setFont(c.getFont().deriveFont(Font.BOLD));

        String columnName = table.getColumnName(column);

        switch (columnName) {
            case "Nível de Risco":
                configurarNivelRisco(c, value);
                break;
            case "Dias de Atraso":
                configurarDiasAtraso(c, value);
                break;
            case "Progresso":
                configurarProgressoRisco(c, value);
                break;
            default:
                configurarPadrao(c);
        }

        return c;
    }

    private void configurarNivelRisco(Component c, Object value) {
        if (value instanceof String) {
            String nivel = (String) value;

            if (nivel.contains("Crítico")) {
                c.setBackground(new Color(255, 150, 150));
                c.setForeground(new Color(100, 0, 0));
            } else if (nivel.contains("Alto")) {
                c.setBackground(new Color(255, 200, 150));
                c.setForeground(new Color(150, 50, 0));
            } else if (nivel.contains("Médio")) {
                c.setBackground(new Color(255, 255, 150));
                c.setForeground(new Color(150, 150, 0));
            } else {
                c.setBackground(new Color(200, 255, 200));
                c.setForeground(new Color(0, 100, 0));
            }
        }
    }

    private void configurarDiasAtraso(Component c, Object value) {
        if (value instanceof String) {
            String diasStr = (String) value;

            try {
                int dias = Integer.parseInt(diasStr.replace(" dias", ""));

                if (dias > 30) {
                    c.setBackground(new Color(255, 150, 150));
                    c.setForeground(new Color(100, 0, 0));
                } else if (dias > 15) {
                    c.setBackground(new Color(255, 200, 150));
                    c.setForeground(new Color(150, 50, 0));
                } else if (dias > 7) {
                    c.setBackground(new Color(255, 255, 150));
                    c.setForeground(new Color(150, 150, 0));
                } else {
                    c.setBackground(new Color(200, 255, 200));
                    c.setForeground(new Color(0, 100, 0));
                }

            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarProgressoRisco(Component c, Object value) {
        if (value instanceof String) {
            String progressoStr = (String) value;
            try {
                double progresso = Double.parseDouble(progressoStr.replace("%", ""));

                if (progresso < 25) {
                    c.setBackground(new Color(255, 150, 150));
                    c.setForeground(new Color(100, 0, 0));
                } else if (progresso < 50) {
                    c.setBackground(new Color(255, 200, 150));
                    c.setForeground(new Color(150, 50, 0));
                } else if (progresso < 75) {
                    c.setBackground(new Color(255, 255, 150));
                    c.setForeground(new Color(150, 150, 0));
                } else {
                    c.setBackground(new Color(200, 255, 200));
                    c.setForeground(new Color(0, 100, 0));
                }

            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarPadrao(Component c) {
        c.setBackground(Color.WHITE);
        c.setForeground(Color.BLACK);
        setHorizontalAlignment(SwingConstants.LEFT);
    }
}