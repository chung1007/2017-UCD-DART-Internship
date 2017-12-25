
#include <SoftwareSerial.h>
#include <avr/interrupt.h>

const int RX_PIN = 8;
const int TX_PIN = 9;
SoftwareSerial bluetooth(RX_PIN, TX_PIN); 
char commandChar;
int counter = 0;
float data [] = {-0.446, -0.444, -0.439, -0.432, -0.423, -0.412, -0.4, -0.388, -0.377, -0.369, -0.364, -0.363, -0.363, -0.363, -0.364, -0.367, -0.372, -0.375, -0.375, -0.373, -0.373, -0.372, -0.372, -0.369, -0.365, -0.361, -0.36, -0.359, -0.358, -0.357, -0.359, -0.364, -0.367, -0.368, -0.37, -0.371, -0.371, -0.368, -0.365, -0.359, -0.356, -0.352, -0.349, -0.346, -0.346, -0.345, -0.348, -0.351, -0.354, -0.354, -0.354, -0.357, -0.357, -0.354, -0.349, -0.342, -0.34, -0.338, -0.336, -0.332, -0.331, -0.332, -0.336, -0.338, -0.34, -0.342, -0.347, -0.35, -0.351, -0.35, -0.348, -0.348, -0.345, -0.338, -0.329, -0.321, -0.317, -0.313, -0.309, -0.305, -0.302, -0.305, -0.308, -0.311, -0.312, -0.314, -0.317, -0.318, -0.316, -0.315, -0.316, -0.317, -0.318, -0.316, -0.318, -0.322, -0.329, -0.336, -0.34, -0.344, -0.35, -0.354, -0.358, -0.356, -0.352, -0.349, -0.344, -0.337, -0.331, -0.326, -0.327, -0.328, -0.329, -0.332, -0.337, -0.348, -0.356, -0.361, -0.366, -0.369, -0.375, -0.378, -0.377, -0.373, -0.372, -0.374, -0.375, -0.374, -0.376, -0.381, -0.392, -0.402, -0.407, -0.411, -0.414, -0.419, -0.42, -0.416, -0.41, -0.406, -0.407, -0.403, -0.399, -0.396, -0.398, -0.402, -0.404, -0.403, -0.403, -0.405, -0.405, -0.402, -0.395, -0.388, -0.382, -0.375, -0.367, -0.357, -0.351, -0.347, -0.342, -0.335, -0.331, -0.328, -0.331, -0.33, -0.328, -0.327, -0.328, -0.331, -0.33, -0.327, -0.324, -0.321, -0.319, -0.313, -0.307, -0.302, -0.301, -0.3, -0.297, -0.294, -0.295, -0.299, -0.301, -0.3, -0.298, -0.296, -0.294, -0.292, -0.289, -0.284, -0.282, -0.278, -0.278, -0.279, -0.281, -0.285, -0.29, -0.298};

int dataSize = 200;
void setup (){
    bluetooth.begin (9600);
    Serial.begin(115200);
    Serial.println("Testing");
}
void setTimerInterrupt(){
  // initialize Timer1
    cli();          // disable global interrupts
    TCCR1A = 0;     // set entire TCCR1A register to 0
    TCCR1B = 0;     // same for TCCR1B
    // set compare match register to desired timer count:
    OCR1A = 77; //compare match register = [ 16,000,000Hz/ (prescaler * desired interrupt frequency) ] - 1
    // turn on CTC mode:
    TCCR1B |= (1 << WGM12);
    // Set CS10 and CS12 bits for 1024 prescaler:
    TCCR1B |= (1 << CS10);
    TCCR1B |= (1 << CS12);
    // enable timer compare interrupt:
    TIMSK1 |= (1 << OCIE1A);
    // enable global interrupts:
    sei();
  
}
void loop () {
  //continuously listen for availble bluetooth connections
  while(bluetooth.available())
    {
        commandChar = bluetooth.read();
        switch(commandChar)
        {
            case '*':
            //send receiver code
            Serial.println("Got receiver code!");
            for(int i = 0; i < dataSize; i++){
              String dataNum =  String(data[i], 3);
              bluetooth.print(dataNum);
              bluetooth.print("/");
            }
            //setTimerInterrupt();
            break;
            }
      }       
}

ISR(TIMER1_COMPA_vect)
{
    bluetooth.print(random(100) + "#");
   
}

