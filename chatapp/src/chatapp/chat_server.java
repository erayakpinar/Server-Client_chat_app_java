/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chatapp;

import java.awt.Color;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import javax.swing.SwingUtilities;

/**
 *
 * @author eraya
 */
public class chat_server extends javax.swing.JFrame {

    /**
     * Creates new form chat_server
     */
    
    
    static ServerSocket ss;
    static HashSet<Socket> clientSockets = new HashSet<>();
    static DataInputStream din;
    static DataOutputStream dout;

    public chat_server() {
        initComponents();
        Color color = new Color(182, 182, 182);
        getContentPane().setBackground(color);
        initializeSocket();
        acceptClients();
    }

    private void initializeSocket() { // SOKET OLUŞTURULUYOR
        try {
            ss = new ServerSocket(1201);
            System.out.println("Sunucu başlatıldı. Bekleniyor...");
        } catch (IOException e) {                          
            System.out.println("Hata oluştu, hata kodunuz: " + e);
        }
    }

    private void acceptClients() { // THREAD YAPISIYLA CLIENTLERIN BAĞLANTILARI KABUL EDİLİYOR
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = ss.accept();
                    System.out.println("İstemci bağlandı: " + clientSocket);

                    DataInputStream clientDin = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream clientDout = new DataOutputStream(clientSocket.getOutputStream());

                    clientSockets.add(clientSocket);

                    readMessage(clientSocket, clientDin, clientDout);
                } catch (IOException e) {
                    System.out.println("Hata oluştu, hata kodunuz: " + e);
                }
            }
        });
        thread.start();
    }

     private void readMessage(Socket clientSocket, DataInputStream clientDin, DataOutputStream clientDout) {
    Thread thread = new Thread(() -> { // GELEN MESAJLARI OKUYAN METOD
        try {
            String msgin = "";
            while (!msgin.equals("exit")) {
                msgin = clientDin.readUTF();
                if (!msgin.startsWith("Server:")) {
                    // Eğer mesaj, server tarafından gönderilmiş bir mesaj değilse
                    sendMessageToAllClients("Client " + clientSocket.getPort() + ": \t" + msgin);
                }
            }
        } catch (Exception e) {
            System.out.println("Hata oluştu, hata kodunuz: " + e);
        }
    });
    thread.start();
}



private void sendMessageToAllClients(String message) { // SERVERİN MESAJINI BÜTÜN CLIENTLERE GÖNDEREN METOD
    for (Socket socket : clientSockets) {
        try {
            DataOutputStream clientDout = new DataOutputStream(socket.getOutputStream());
            clientDout.writeUTF(message);
            clientDout.flush();
        } catch (IOException e) {
            System.out.println("Hata oluştu, hata kodunuz: " + e);
        }
    }
    updateTextArea(message);  // Serverın ekranına da mesajı ekleyelim
    MessageLogger.saveMessageToDatabase("Server", message);
}

    private void updateTextArea(String message) { // SERVER MESAJ EKRANINI GÜNCELLEYEN METOD
        SwingUtilities.invokeLater(() -> {
            msg_area.setText(msg_area.getText().trim() + "\n" + message);
        });
    }

    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        msg_area = new javax.swing.JTextArea();
        msg_send = new javax.swing.JButton();
        msg_text = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CHAT-SERVER");
        setBackground(new java.awt.Color(102, 255, 102));

        msg_area.setColumns(20);
        msg_area.setRows(5);
        jScrollPane1.setViewportView(msg_area);

        msg_send.setText("GÖNDER");
        msg_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msg_sendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(msg_text)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(msg_send))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msg_text)
                    .addComponent(msg_send, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void msg_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msg_sendActionPerformed
         try {
        String msgout = "";
        msgout = msg_text.getText().trim();
        sendMessageToAllClients("Server: " + msgout);        
    } catch (Exception e) {
        System.out.println("Hata oluştu, hata kodunuz: " + e);
    }      
        
    }//GEN-LAST:event_msg_sendActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(chat_server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(chat_server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(chat_server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(chat_server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new chat_server().setVisible(true);
            }
        });
         
        
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextArea msg_area;
    private javax.swing.JButton msg_send;
    private javax.swing.JTextField msg_text;
    // End of variables declaration//GEN-END:variables

    
}
