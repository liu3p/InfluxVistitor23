package gui.components.actionpanels;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.*;
import java.util.*;


public class SftpUtil {

    private String loginName = "ems";
    private String loginPassword = "qwer1234";
    private String server = "192.168.0.100";
    private Integer port = 22;


    public static void main(String[] args) {
        SftpUtil sftpUtil = new SftpUtil();
        //�ϴ��ļ�
        sftpUtil.uploadFile();
        //�����ļ�
        sftpUtil.downloadFile();
        //д�ļ�
        sftpUtil.writeFile();
        //���ļ�
        sftpUtil.readFile();
        //ɾ���ļ�
        sftpUtil.deleteFile();
    }

    public void setServerPort(String server_ip,int port)
    {
        this.server = server_ip;
        this.port   = port;
    }

    /**
     * ���ӵ�½Զ�̷�����
     *
     * @return
     */
    public ChannelSftp connect()  {
        JSch jSch = new JSch();
        Session session = null;
        ChannelSftp sftp = null;
        try {
            session = jSch.getSession(loginName, server, port);
            session.setPassword(loginPassword);
            session.setConfig(this.getSshConfig());
            session.connect();

            sftp = (ChannelSftp)session.openChannel("sftp");
            sftp.connect();

            System.out.println("�����"+session.equals(sftp.getSession()));
        } catch (Exception e) {
            return null;
        }
        return sftp;
    }

    /**
     * ��ȡ��������
     * @return
     */
    private Properties getSshConfig() {
        Properties sshConfig =  new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        return sshConfig;
    }


    /**
     * �ر�����
     * @param sftp
     */
    public void disconnect(ChannelSftp sftp) {
        try {
            if(sftp!=null){
                if(sftp.getSession().isConnected()){
                    sftp.getSession().disconnect();
                }
            }
        } catch (Exception e) {
            //log.error("�ر���sftp�������Ự�����쳣",e);
        }
    }



    /**
     * ����Զ��sftp�������ļ�
     *
     * @return
     */
    public void downloadFile() {
        FileOutputStream output = null;
        ChannelSftp sftp = null;
        try {
            sftp = connect();
            if(sftp == null){
                System.out.println("Get connection failed");
                return ;
            }
            //sftp���������ļ�·��
            String remoteFilename = "/users/ems/theta/deployment/etc/scada/oid_name.txt";
            //����������·��
            File localFile = new File("oid_name.txt");
            output = new FileOutputStream(localFile);

            sftp.get(remoteFilename, output);
            System.out.println("�ɹ������ļ�,����·����" + localFile.getAbsolutePath());
        } catch (Exception e) {
            //log.error("�����ļ��쳣!",e);
        } finally {
            try {
                if (null != output) {
                    output.flush();
                    output.close();
                }
                // �ر�����
                disconnect(sftp);
            } catch (IOException e) {
            }
        }
    }



    /**
     * ��ȡԶ��sftp�������ļ�
     *
     * @return
     */
    public void readFile() {
        InputStream in = null;
        ArrayList<String> strings = new ArrayList<>();
        ChannelSftp sftp = null;
        try {
            sftp = connect();
            if(sftp == null){
                return;
            }
            String remotePath = "/test1/";
            String remoteFilename = "����1.txt";
            sftp.cd(remotePath);
            if(!listFiles(remotePath).contains(remoteFilename)){
                //log.error("no such file");
                return;
            }
            in = sftp.get(remoteFilename);
            if (in != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
                String str = null;
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }
            }else{
                //log.error("inΪ�գ����ܶ�ȡ��");
            }
        } catch (Exception e) {
            //log.error("�����ļ�ʱ�쳣!",e);
        }  finally {
            try {
                if(in !=null){
                    in.close();
                }
                // �ر�����
                disconnect(sftp);
            } catch (Exception e) {
                //log.error("�ر��ļ���ʱ�����쳣!",e);
            }
        }
    }


    /**
     * д�ļ���Զ��sftp������
     *
     * @return
     */
    public void writeFile(){
        ChannelSftp sftp = null;
        ByteArrayInputStream input = null;
        try {
            sftp = connect();
            if(sftp == null){
                return;
            }
            // ���ķ�����Ŀ¼
            String remotePath = "/test1/";
            sftp.cd(remotePath);
            // �����ļ�
            String remoteFilename = "д�ļ�.txt";
            String content = "��������";
            input = new ByteArrayInputStream(content.getBytes());
            sftp.put(input, remoteFilename);
        } catch (Exception e) {
            //log.error("�����ļ�ʱ���쳣!",e);
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
                // �ر�����
                disconnect(sftp);
            } catch (Exception e) {
                //log.error("�ر��ļ�ʱ����!",e);
            }
        }
    }



    /**
     * �ϴ��ļ���sftp������
     * @return
     */
    public void uploadFile() {
        FileInputStream fis = null;
        ChannelSftp sftp = null;
        // �ϴ��ļ�����������Ŀ¼
        String remotePath = "./file/sftp/��sftp������������.txt";
        String remoteFilename = "/test1/�ϴ���sftp������.txt";
        try {
            sftp = connect();
            if(sftp == null){
                return ;
            }

            File localFile = new File(remotePath);
            fis = new FileInputStream(localFile);
            //�����ļ�
            sftp.put(fis, remoteFilename);
            //log.info("�ɹ��ϴ��ļ�" );
        } catch (Exception e) {
            //log.error("�ϴ��ļ�ʱ�쳣!",e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                // �ر�����
                disconnect(sftp);
            } catch (Exception e) {
                //log.error("�ر��ļ�ʱ����!",e);
            }
        }
    }


    /**
     * ����Զ���ļ�
     *
     * @param remotePath
     * @return
     * @throws Exception
     */
    public List<String> listFiles(String remotePath){
        List<String> ftpFileNameList = new ArrayList<String>();
        ChannelSftp.LsEntry isEntity = null;
        String fileName = null;
        ChannelSftp sftp = null;
        try{
            sftp = connect();
            if(sftp == null){
                return null;
            }
            Vector<ChannelSftp.LsEntry> sftpFile = sftp.ls(remotePath);
            Iterator<ChannelSftp.LsEntry> sftpFileNames = sftpFile.iterator();
            while (sftpFileNames.hasNext()) {
                isEntity = (ChannelSftp.LsEntry) sftpFileNames.next();
                fileName = isEntity.getFilename();
                ftpFileNameList.add(fileName);
            }
            return ftpFileNameList;
        }catch (Exception e){
            //log.error("������ѯsftp���������ļ��쳣",e);
            return null;
        }finally {
            disconnect(sftp);
        }

    }


    /**
     * ɾ��Զ���ļ�
     * @return
     */
    public void deleteFile() {
        boolean success = false;
        ChannelSftp sftp = null;
        try {
            sftp = connect();
            if(sftp == null){
                return;
            }
            String remotePath = "/test1/";
            String remoteFilename = "limit.lua";
            // ���ķ�����Ŀ¼
            sftp.cd(remotePath);
            //�ж��ļ��Ƿ����
            if(listFiles(remotePath).contains(remoteFilename)){
                // ɾ���ļ�
                sftp.rm(remoteFilename);
                //log.info("ɾ��Զ���ļ�" + remoteFilename + "�ɹ�!");
            }

        } catch (Exception e) {
            //log.error("ɾ���ļ�ʱ���쳣!",e);
        } finally {
            // �ر�����
            disconnect(sftp);
        }
    }

}
