package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DesempenhoTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String columnName = table.getColumnName(column);

        switch (columnName) {
            case "Taxa Conclusão":
                configurarTaxaConclusao(c, value);
                break;
            case "Produtividade":
                configurarProdutividade(c, value);
                break;
            case "Tarefas Totais":
            case "Concluídas":
            case "Em Andamento":
            case "Atrasadas":
                configurarNumeros(c, value);
                break;
            default:
                configurarPadrao(c);
        }

        return c;
    }

    private void configurarTaxaConclusao(Component c, Object value) {
        if (value instanceof String) {
            String taxaStr = (String) value;
            try {
                double taxa = Double.parseDouble(taxaStr.replace("%", ""));

                if (taxa >= 90) {
                    c.setBackground(new Color(220, 255, 220));
                    c.setForeground(new Color(0, 100, 0));
                } else if (taxa >= 75) {
                    c.setBackground(new Color(255, 255, 220));
                    c.setForeground(new Color(150, 150, 0));
                } else if (taxa >= 50) {
                    c.setBackground(new Color(255, 220, 200));
                    c.setForeground(new Color(150, 75, 0));
                } else {
                    c.setBackground(new Color(255, 200, 200));
                    c.setForeground(new Color(150, 0, 0));
                }

                c.setFont(c.getFont().deriveFont(Font.BOLD));

            } catch (NumberFormatException e) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
    }

    private void configurarProdutividade(Component c, Object value) {
        if (value instanceof String) {
            String prod = (String) value;
            c.setFont(c.getFont().deriveFont(Font.BOLD));

            if (prod.contains("Excelente")) {
                c.setBackground(new Color(220, 255, 220));
                c.setForeground(new Color(0, 100, 0));
            } else if (prod.contains("Boa")) {
                c.setBackground(new Color(255, 255, 220));
                c.setForeground(new Color(150, 150, 0));
            } else if (prod.contains("Regular")) {
                c.setBackground(new Color(255, 220, 200));
                c.setForeground(new Color(150, 75, 0));
            } else {
                c.setBackground(new Color(255, 200, 200));
                c.setForeground(new Color(150, 0, 0));
            }
        }
    }

    private void configurarNumeros(Component c, Object value) {
        c.setBackground(new Color(240, 240, 255));
        c.setForeground(Color.BLUE);
        c.setFont(c.getFont().deriveFont(Font.BOLD));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void configurarPadrao(Component c) {
        c.setBackground(Color.WHITE);
        c.setForeground(Color.BLACK);
        setHorizontalAlignment(SwingConstants.LEFT);
    }
}