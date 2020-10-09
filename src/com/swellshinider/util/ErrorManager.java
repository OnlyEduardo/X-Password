package com.swellshinider.util;

import javax.swing.*;

public class ErrorManager {

    public static final int NORMAL_SEVERITY = 0;
    public static final int GRAVE_SEVERITY = 1;
    public static final int FALTAL_SEVERITY = 2;

    /**
     *
     * @param messageError Descrição do erro
     * @param fromClass Nome da classe onde ocorreu o erro
     * @param fromFunction Nome da função onde ocorreu o erro
     * @param exception Exception ocorrida
     * @param severity Nível de severidade
     */
    public static void showErrorManager(String messageError, String fromClass, String fromFunction, Exception exception, int severity){
        String severityMessage;

        if(exception == null)
            exception = new RuntimeException();

        switch (severity){
            case 1:
                severityMessage = "GRAVE o programa não executará corretamente.";
                break;
            case 2:
                severityMessage = "FALTAL o programa pode fechar a qualquer instante.";
                break;
            case 0:
            default:
                severityMessage = "NORMAL o programa pode encontrar dificuldades para funcionar.";
                break;
        }

        String message = String.format(
                "O programa encontrou um erro, nível = %s\n\n" +
                        "Tente reiniciar o programa, se o erro persistir " +
                        "entre em contato com o desenvolvedor <eduardoleal.contact@gmail.com>.\n" +
                        "E envie esse erro:\n" +
                        "Erro: %s.\n" +
                        "FromClass: %s.\n" +
                        "FromFunction: %s \n" +
                        "Exception: %s \n",
                severityMessage,
                messageError,
                fromClass,
                fromFunction,
                exception
        );

        JOptionPane.showMessageDialog(new Box(5),
                message, "Ocorreu um erro",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @param messageError Descrição do erro
     * @param fromClass Nome da classe onde ocorreu o erro
     * @param fromFunction Nome da função onde ocorreu o erro
     * @param severity Nível de severidade
     */
    public static void showErrorManager(String messageError, String fromClass, String fromFunction, int severity){
        showErrorManager(messageError, fromClass, fromFunction, null, severity);
    }
}
