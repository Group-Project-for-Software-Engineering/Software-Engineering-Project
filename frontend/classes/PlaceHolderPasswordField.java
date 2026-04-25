/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: PlaceHolderPasswordField.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: February 2026
 * The PlaceHolderPasswordField class extends the JPasswordField class to add a placeholder text to the password field.
 */
package classes;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// ---------------------------------------------------------------
public class PlaceHolderPasswordField extends JPasswordField {

    private final String placeHolder;

    // ---------------------------------------------------------------
    /**
     * constructor generates password field + placeholder text
     */
    public PlaceHolderPasswordField(String text, int cols) {
        super(cols);
        placeHolder = text;

        setBorder(BorderFactory.createCompoundBorder(
                getBorder(), BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Repaint whenever text changes so placeholder updates correctly
        // three different kinds of text changes
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                repaint();
            }
        });

        // Repaint when user clicks in or out of the textfield
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    // ---------------------------------------------------------------
    /**
     * Draws placeholder text
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw placeholder only when empty AND not clicked on by user
        if (getPassword().length == 0 && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            int x = getInsets().left;
            int y = getHeight() / 2 + getFont().getSize() / 2 - 2;
            g2.drawString(placeHolder, x, y);
            g2.dispose();
        }
    }
}
