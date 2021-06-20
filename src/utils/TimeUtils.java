

package utils;

public class TimeUtils{


    public static String convertSecondsToFormattedText(int seconds){
        int segundo = seconds%60; // Segundos = resto do contador.  
        int minuto = seconds/60; // Minuto = divisão do contador por 60.
        minuto%=60; 
        var timerPlayer = String.format("%02d:%02d", minuto, segundo); // Formatação do contador.
        return timerPlayer;
    }
}