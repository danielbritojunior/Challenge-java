package model;

public class Reuniao {
    private Integer id;
    private String interlocutor;
    private String textoBruto;


    public Reuniao(Integer id, String interlocutor, String textoBruto) {
        this.id = id;
        this.interlocutor = interlocutor;
        this.textoBruto = textoBruto;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getInterlocutor() { return interlocutor; }
    public void setInterlocutor(String interlocutor) { this.interlocutor = interlocutor; }

    public String getTextoBruto() { return textoBruto; }
    public void setTextoBruto(String textoBruto) { this.textoBruto = textoBruto; }
}