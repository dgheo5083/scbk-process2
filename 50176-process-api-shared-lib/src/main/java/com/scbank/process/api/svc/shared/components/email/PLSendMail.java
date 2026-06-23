package com.scbank.process.api.svc.shared.components.email;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PLSendMail extends Thread {
    private Socket socket;
    private DataOutputStream out;
    private String clerkNo;
    private String subject;
    private String content;
    private String sender = PropertiesUtils.getString("SMTP_OUTLOOK_SENDER"); // BILL HELPERDEST
    private String senderDomain = PropertiesUtils.getString("SMTP_OUTLOOK_DOMAIN"); // BILL HELPERDEST 도메인
    private boolean responseCheck = false;

    public PLSendMail() {

    }

    public PLSendMail(String clerkNo, String subject, String content) {
        this.clerkNo = clerkNo;
        this.subject = subject;
        this.content = content;
    }

    public PLSendMail(String clerkNo, String subject, String content, String sender) {
        this.clerkNo = clerkNo;
        this.subject = subject;
        this.content = content;
        this.sender = sender;
    }

    public PLSendMail(String clerkNo, String subject, String content, String sender, String senderDomain) {
        this.clerkNo = clerkNo;
        this.subject = subject;
        this.content = content;
        this.sender = sender;
        this.senderDomain = senderDomain;
    }

    public void sendMail() throws Exception {

        int delay = 1000;
        String smtpHost = PropertiesUtils.getString("SMTP_OUTLOOK_IP");
        String port = PropertiesUtils.getString("SMTP_OUTLOOK_PORT");
        String SenderURl = sender + senderDomain;
        String recipientURl = clerkNo + ".kr" + PropertiesUtils.getString("SMTP_OUTLOOK_DOMAIN");

        log.info("Create Socket");
        log.info("Connecting {}", smtpHost);
        try {
            socket = new Socket(smtpHost, Integer.parseInt(port));
        } catch (Exception e) {
            log.info("Connect Error : " + e.getMessage());
            if (null != socket) {
                socket.close();
            }
            return;
        }

        final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());

        // 서버 응답
        log.info("Response Thread start");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    String line;
                    while ((line = br.readLine()) != null) {
                        Thread.sleep(100);
                        log.info("response : {}", line);

                        if (responseCheck) {
                            break;
                        }
                    }
                    log.info("SMTP Response while Break @@@@@@@@@@@@@@@@@@");
                } catch (Exception e) {
                    interrupt();
                    log.info("Response Err : {}", e.getMessage());
                }
                return;
            }
        });

        try {
            thread.start();
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        String _Subject = "Subject:" + subject + "\r\nFrom:" + SenderURl + "\r\nTo:" + recipientURl + "\r\n";
        String _content = content + "\r\n";

        try {
            // 메일 전송
            log.info("Mail send Start");
            log.debug("====Mail Send Start ====");

            out.writeBytes("EHLO " + smtpHost + "\r\n");
            Thread.sleep(delay);
            out.writeBytes("MAIL FROM:<" + SenderURl + ">\r\n");
            Thread.sleep(delay);
            out.writeBytes("RCPT TO:<" + recipientURl + ">\r\n");
            Thread.sleep(delay);
            out.writeBytes("DATA\r\n");
            Thread.sleep(delay);

            log.info("Subject : {}", _Subject);
            log.debug("Subject : {}", _Subject);

            out.write(_Subject.getBytes("UTF-8"));
            out.writeBytes("\r\n\r\n");

            log.info("content : {}", _content);
            log.debug("content : {}", _content);
            out.write(_content.getBytes("UTF-8"));

            out.writeBytes("\r\n\r\n");
            Thread.sleep(delay);
            out.writeBytes(".\r\n");
            Thread.sleep(delay);
            out.writeBytes("QUIT\r\n");
            Thread.sleep(delay);
            responseCheck = true;
        } catch (Exception e) {
            this.interrupt();
            log.info(e.getMessage());
        } finally {
            if (br != null) {
                br.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }

    public void run() {
        try {
            sendMail();
        } catch (Exception e) {
            log.info("sendMail Error : {}", e.getMessage());
        }

        return;
    }

}
