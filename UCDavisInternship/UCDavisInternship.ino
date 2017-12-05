
#include <SoftwareSerial.h>
#include <avr/interrupt.h>

const int RX_PIN = 8;
const int TX_PIN = 9;
SoftwareSerial bluetooth(RX_PIN, TX_PIN); 
char commandChar;
int counter = 0;

void setup (){
    bluetooth.begin (9600);
    Serial.begin(9600);
    Serial.print("Testing");
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
  while(bluetooth.available())
    {
        commandChar = bluetooth.read();
        switch(commandChar)
        {
            case '*':
            Serial.print("Initializer Received!");
            setTimerInterrupt();
            break;
            }
      }       
}

ISR(TIMER1_COMPA_vect)
{
    bluetooth.print(random(100));
    bluetooth.print("/");
}

