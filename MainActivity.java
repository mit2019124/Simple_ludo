package com.example.ludo;


import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    /*
        playerId
        0 is Red
        1 is Green
        2 is yellow
        3 is Blue
     */
    int nim;                            //nim is the next value of the coin after move
    int playerId = 0;
    boolean cutCoin = false;            //it is true when any coin cut another coin so that player will get an extra roll
    boolean activeOpen = false;         //if true then player can one his coin from box
    boolean activeMove = false;         //if true then player can move their coin
    boolean activeRoll = false;         //if true then player can roll dice on their turn
    List<Integer> list = Arrays.asList(0,8,13,21,26,34,39,47);
    public Set<Integer> defstop = new HashSet<>(list);      //it define position of stops
    //int tapImg = Integer.parseInt(img.getTag().toString());
    public int rd ;                     //store current value of roll dice
    boolean activeGame = false;         //if true then game start
    byte[] home ={0,0,0,0};              //store value of no of coin passed
    boolean[] colorStatus = {false,false,false,false};      //it store color status or player status true if any coin open by that player
    boolean[][] coinStatus = {{false,false,false,false},{false,false,false,false}, {false,false,false,false}, {false,false,false,false}};
    //it store identity of coin open in field
    int[][] coinPosition = {{-1,-1,-1,1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
    //it store tag number of coin in field
    //static int[] restPosition = {0,8,13,21,26,34,39,47 };
    byte winPosition = 0;
    boolean[] winColor = {false,false,false,false};
    private TextToSpeech myText;
    private final int MY_DATA=0;

    /*
        ****Initial state****
        #All four colour coin in box;
        #Chance of Red to play the game first;
        #Player is able to roll dice now
        *
        * It is call by tapping any key on screen
        * rollDice or openCoin or moveStep

     */
    void initialState(View view){
        ((ImageView)findViewById(R.id.fim1)).setImageResource(R.drawable.redc);
        ((ImageView)findViewById(R.id.fim2)).setImageResource(R.drawable.redc);
        ((ImageView)findViewById(R.id.fim3)).setImageResource(R.drawable.redc);
        ((ImageView)findViewById(R.id.fim4)).setImageResource(R.drawable.redc);
        ((ImageView)findViewById(R.id.fim5)).setImageResource(R.drawable.greenc);
        ((ImageView)findViewById(R.id.fim6)).setImageResource(R.drawable.greenc);
        ((ImageView)findViewById(R.id.fim7)).setImageResource(R.drawable.greenc);
        ((ImageView)findViewById(R.id.fim8)).setImageResource(R.drawable.greenc);
        ((ImageView)findViewById(R.id.fim9)).setImageResource(R.drawable.bluec);
        ((ImageView)findViewById(R.id.fim10)).setImageResource(R.drawable.bluec);
        ((ImageView)findViewById(R.id.fim11)).setImageResource(R.drawable.bluec);
        ((ImageView)findViewById(R.id.fim12)).setImageResource(R.drawable.bluec);
        ((ImageView)findViewById(R.id.fim13)).setImageResource(R.drawable.yellowc);
        ((ImageView)findViewById(R.id.fim14)).setImageResource(R.drawable.yellowc);
        ((ImageView)findViewById(R.id.fim15)).setImageResource(R.drawable.yellowc);
        ((ImageView)findViewById(R.id.fim16)).setImageResource(R.drawable.yellowc);
        ((ImageView)findViewById(R.id.pass1)).setImageResource(0);
        ((ImageView)findViewById(R.id.pass2)).setImageResource(0);
        ((ImageView)findViewById(R.id.pass3)).setImageResource(0);
        ((ImageView)findViewById(R.id.pass4)).setImageResource(0);
        ((ImageView)findViewById(R.id.dim0)).setImageResource(R.drawable.dice6);

        playerId = 0;
        activeGame =  true;
        activeMove = false;
        activeOpen =  false;
        activeRoll = true;
        winPosition = 0;
        cutCoin = false;
        home = new byte[]{0,0,0,0};
        winColor = new boolean[]{false,false,false,false};
        coinPosition = new int[][]{{-1,-1,-1,1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
        colorStatus = new boolean[]{false,false,false,false};
        coinStatus = new boolean[][]{{false,false,false,false},{false,false,false,false}, {false,false,false,false}, {false,false,false,false}};
        //t1.setText("Red's Turn");
    }
    /*public void updateState(View view){
        for(int i = 0; i > 4; i++){
            for(int j = 0; j > 4; j++){
                if(coinStatus[i][j]){
                    
                }
            }
        }
    }*/
   /* public void gameOperation(){
        if (colorStatus[playerId] == true || rd == 6) {
            if (rd != 6 && !activeOpen) {
                playerId = (playerId + 1) % 4;
                ((ImageView) findViewById(R.id.dice1)).setImageResource(0);
                ((ImageView) findViewById(R.id.dice2)).setImageResource(R.drawable.dice6);
            }
                activeOpen = true;
                activeMove = true;

       }
    }*/
    /*
        *******nextMove() function *********
        * next move is executed only if activeOpen activeMove and activeRoll is false
        * because not to skip chance of active player
        * it changes player id and position of dice to next player for roll

     */
    public void setCurrentStatus(){
        TextView t1 = findViewById(R.id.t1);
        TextView t2 = findViewById(R.id.t2);
        TextView t3 = findViewById(R.id.t3);
        TextView t4 = findViewById(R.id.t4);
        for(int i = 0; i < 4; i++){
            if(!winColor[i] && i!=playerId){
                switch (i){
                    case 0: t1.setText("Player 1");
                            break;
                    case 1: t2.setText("Player 2");
                            break;
                    case 2: t3.setText("Player 3");
                            break;
                    case 3: t4.setText("Player 4");
                            break;

                }
            }
        }
    }
    /*private void speakWords(String speech){
        myText.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
    }*/
    public void nextMove(){
        TextView t1 = findViewById(R.id.t1);
        TextView t2 = findViewById(R.id.t2);
        TextView t3 = findViewById(R.id.t3);
        TextView t4 = findViewById(R.id.t4);
        final String[] State ={"Red's Turn","Green's Turn","Yellow's Turn","Blue's Turn"};
        /*myText = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    myText.setLanguage(Locale.UK);
                }
            }
        });*/
        if(!activeOpen && !activeMove && !activeRoll){
            playerId = (playerId + 1) % 4;
            if(home[playerId] == 4) {
                nextMove();
            }// % prevent playerID from going beyond available players
            activeRoll = true;
            if(playerId == 1){
                t2.setText("Green's Turn");
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim1)).setImageResource(R.drawable.dice6);
            }
            else if (playerId == 2){
                t3.setText("Yellow's Turn");
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim2)).setImageResource(R.drawable.dice6);
            }
            else if(playerId ==3){
                t4.setText("Blue's Turn");
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim3)).setImageResource(R.drawable.dice6);
            }
            else {
                t1.setText("Red's Turn");
                setCurrentStatus();
                ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
                ((ImageView)findViewById(R.id.dim0)).setImageResource(R.drawable.dice6);
            }
            Toast.makeText(getApplicationContext(),State[playerId],Toast.LENGTH_SHORT).show();
            myText.speak(State[playerId],TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    /*
        *********rollDice() function**********
            * it allow only active player to roll dice
            * it utilise random function
            * it executed when activeGame is true and activeRoll is true
            * if player already open their coin then they will able to get a move option by true value of activeMove
            * if dice roll for 6 then player can also get option to open their coin
            * at last nextMove is call because if player did't get 6 and not any open coin then next player get chance
            * if activeMove of activeOpen is true then nextMove did nothing
     */
    public void rollDice(View view) throws InterruptedException {
       if(!activeGame){
           initialState(view);
       } else if(activeRoll){
               ImageView img = (ImageView) view;
               /*final String State[]={"Red's Turn","Green's Turn","Yellow's Turn","Blue's Turn"};
               myText = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                   @Override
                   public void onInit(int i) {
                       if(i != TextToSpeech.ERROR){
                           myText.setLanguage(Locale.UK);
                       }
                   }
               });
               View.OnClickListener clickListener = new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Toast.makeText(getApplicationContext(),State[playerId],Toast.LENGTH_SHORT).show();
                       myText.speak(State[0],TextToSpeech.QUEUE_FLUSH,null);
                   }
               };*/
               if(Integer.parseInt(img.getTag().toString())== ((playerId+1)*111)) {
                   img.setRotationY(-1000f);
                   rd = (int) (1 + (6 * Math.random()));
                   switch (rd) {
                       case 1:
                           img.setImageResource(R.drawable.dice1);
                           break;
                       case 2:
                           img.setImageResource(R.drawable.dice2);
                           break;
                       case 3:
                           img.setImageResource(R.drawable.dice3);
                           break;
                       case 4:
                           img.setImageResource(R.drawable.dice4);
                           break;
                       case 5:
                           img.setImageResource(R.drawable.dice5);
                           break;
                       case 6:
                           img.setImageResource(R.drawable.dice6);
                           break;
                   }
                   img.animate().rotationYBy(1000f).setDuration(500);
                   //Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
                   //img.startAnimation(animation);
                   activeRoll = false;
                   cutCoin = false;
                   if(colorStatus[playerId]) {
                       int min = 1000;
                       for (int i=0; i<4 ; i++) {
                           if(coinStatus[playerId][i] == false && rd == 6){
                               activeOpen = true;
                           }
                           if (coinPosition[playerId][i] < min && coinPosition[playerId][i] >= 0) {
                               min = coinPosition[playerId][i];
                           }
                       }
                       if(min+rd>60 && (min+rd)%100>5){
                           activeMove = false;
                           nextMove();
                       }
                       else{
                           activeMove = true;
                       }
                   }
                   else if (rd == 6) {
                       activeOpen = true;
                   }
                   /*if (colorStatus[playerId]) {
                       activeMove = true;
                   */
                   else {
                       nextMove();
                   }
               }
       }
    }
    /*

     */
    public  void openCoin(View view){
        if(!activeGame){
            initialState(view);
        }
        else if(activeOpen){
                ImageView img = (ImageView) view;
                int im = Integer.parseInt(img.getTag().toString());
                int key = 10 * (playerId+6);
                if(im >= key && im <key + 4) {
                    int pos;
                    pos = (im % 10);
                    img.setImageResource(0);
                    if (coinStatus[playerId][pos] == false) {
                        coinStatus[playerId][pos] = true;
                        // String str1 = valueOf(pos);
                        //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                        colorStatus[playerId] = true;
                        activeOpen = false;
                        activeMove = false;
                        activeRoll = true;
                        if (playerId == 0) {
                            //((ImageView) findViewById(R.id.a2)).setImageResource(R.drawable.redc);
                            //coinPosition[playerId][pos] = Integer.parseInt((findViewById(R.id.a2)).getTag().toString());
                            coinPosition[playerId][pos] = 0;
                            reposition(coinPosition[playerId][pos]);
                            //String str1 = valueOf(coinPosition[playerId][pos]);
                            //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                        } else if (playerId == 1) {
                            //((ImageView) findViewById(R.id.b6)).setImageResource(R.drawable.greenc);
                            //coinPosition[playerId][pos] = Integer.parseInt(findViewById(R.id.b6).getTag().toString());
                            coinPosition[playerId][pos] = 13;
                            reposition(coinPosition[playerId][pos]);
                            //String str1 = valueOf(coinPosition[playerId][pos]);
                            //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                        } else if (playerId == 2) {
                            //((ImageView) findViewById(R.id.c17)).setImageResource(R.drawable.yellowc);
                            //coinPosition[playerId][pos] = Integer.parseInt((findViewById(R.id.c17)).getTag().toString());
                            coinPosition[playerId][pos] = 26;
                            reposition(coinPosition[playerId][pos]);
                            //String str1 = valueOf(coinPosition[playerId][pos]);
                            // Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                        } else {
                            //((ImageView) findViewById(R.id.d13)).setImageResource(R.drawable.bluec);
                            //coinPosition[playerId][pos] = Integer.parseInt((findViewById(R.id.d13)).getTag().toString());
                            coinPosition[playerId][pos] = 39;
                            reposition(coinPosition[playerId][pos]);
                            //String str1 = valueOf(coinPosition[playerId][pos]);
                            //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                        }
                        //nextMove();
                    }
                }
        }
    }
    public void setColorStatus(int id){
        colorStatus[id] = false;
        for(int k=0; k<4; k++){
            if(coinStatus[id][k] == true){
                if(coinPosition[id][k] !=105 && coinPosition[id][k] != 205 && coinPosition[id][k] != 305 && coinPosition[id][k] != 405) {
                    colorStatus[id] = true;
                    break;
                }
            }
        }
    }
   /* public void pairingfun(View view){
        int count=0;
        for(int i:coinPosition[playerId]){
            if(i == nim){
                count+=1;
            }
        }
        if(count == 2){

        }
    }*/
    public  void cutting(View view){
        for(int i = 0; i<4;i++){
            for(int j = 0;j < 4; j++){
                  if(coinPosition[i][j] == nim){
                      if(i != playerId){
                          cutCoin = true;
                          coinPosition[i][j] = -1;
                          coinStatus[i][j] = false;
                          colorStatus[i] = false;
                          int cl = ((i+6)*10)+j;
                          switch(cl){
                            case 60: ((ImageView)findViewById(R.id.fim4)).setImageResource(R.drawable.redc);
                                break;
                            case 61: ((ImageView)findViewById(R.id.fim1)).setImageResource(R.drawable.redc);
                                break;
                            case 62: ((ImageView)findViewById(R.id.fim2)).setImageResource(R.drawable.redc);
                                break;
                            case 63: ((ImageView)findViewById(R.id.fim3)).setImageResource(R.drawable.redc);
                                break;
                            case 70: ((ImageView)findViewById(R.id.fim8)).setImageResource(R.drawable.greenc);
                                break;
                            case 71: ((ImageView)findViewById(R.id.fim5)).setImageResource(R.drawable.greenc);
                                break;
                            case 72: ((ImageView)findViewById(R.id.fim6)).setImageResource(R.drawable.greenc);
                                break;
                            case 73: ((ImageView)findViewById(R.id.fim7)).setImageResource(R.drawable.greenc);
                                break;
                            case 80: ((ImageView)findViewById(R.id.fim16)).setImageResource(R.drawable.yellowc);
                                break;
                            case 81: ((ImageView)findViewById(R.id.fim13)).setImageResource(R.drawable.yellowc);
                                break;
                            case 82: ((ImageView)findViewById(R.id.fim14)).setImageResource(R.drawable.yellowc);
                                break;
                            case 83: ((ImageView)findViewById(R.id.fim15)).setImageResource(R.drawable.yellowc);
                                break;
                            case 90: ((ImageView)findViewById(R.id.fim12)).setImageResource(R.drawable.bluec);
                                break;
                            case 91: ((ImageView)findViewById(R.id.fim9)).setImageResource(R.drawable.bluec);
                                break;
                            case 92: ((ImageView)findViewById(R.id.fim10)).setImageResource(R.drawable.bluec);
                                break;
                            case 93: ((ImageView)findViewById(R.id.fim11)).setImageResource(R.drawable.bluec);
                                break;
                          }
                          setColorStatus(i);
                          /*for(int k=0; k<4; k++){
                              if(coinStatus[i][k] == true){
                                  if(coinPosition[i][k] !=105 && coinPosition[i][k] != 205 && coinPosition[i][j] != 305 && coinPosition[i][j] != 405) {
                                      colorStatus[i] = true;
                                      break;
                                  }
                              }
                          }*/
                      }
                      else{
                          reposition(nim);
                      }
                  }
            }
        }

    }
    public void displayCoins(ImageView img, byte[] count){
        boolean b = (count[0] != 0) && (count[1] != 0) && (count[2] != 0) && (count[3] != 0);
        if(b){
            img.setImageResource(R.drawable.r1g1y1b1);
        }
        else if(count[0]!=0 && count[1]!=0 && count[2]!=0){
            img.setImageResource(R.drawable.r1g1y1);
        }
        else if(count[0]!=0 && count[1]!=0 && count[3]!=0){
            img.setImageResource(R.drawable.r1g1b1);
        }
        else if(count[0]!=0 && count[2]!=0 && count[3] !=0){
            img.setImageResource(R.drawable.r1y1b1);
        }
        else if(count[1]!=0 && count[2]!=0 && count[3] !=0){
            img.setImageResource(R.drawable.g1y1b1);
        }
        else if(count[0] == 3 && count[1] == 1){
            img.setImageResource(R.drawable.r3g1);
        }
        else if(count[0] == 3 && count[2] == 1){
            img.setImageResource(R.drawable.r3y1);
        }
        else if(count[0] == 3 && count[3] == 1){
            img.setImageResource(R.drawable.r3b1);
        }
        else if(count[0] == 2 && count[1] == 1){
            img.setImageResource(R.drawable.r2g1);
        }
        else if(count[0] == 2 && count[2] == 1){
            img.setImageResource(R.drawable.r2y1);
        }
        else if(count[0] == 2 && count[3] == 1){
            img.setImageResource(R.drawable.r2b1);
        }
        else if(count[0] == 2 && count[1] == 2){
            img.setImageResource(R.drawable.r2g2);
        }
        else if(count[0] == 2 && count[2] == 2){
            img.setImageResource(R.drawable.r2y2);
        }
        else if(count[0] == 2 && count[3] == 2){
            img.setImageResource(R.drawable.r2b2);
        }
        else if(count[1] == 3 && count[0] == 1){
            img.setImageResource(R.drawable.r1g3);
        }
        else if(count[1] == 3 && count[2] == 1){
            img.setImageResource(R.drawable.g3y1);
        }
        else if(count[1] == 3 && count[3] == 1){
            img.setImageResource(R.drawable.g3b1);
        }
        else if(count[1] == 2 && count[2] == 2){
            img.setImageResource(R.drawable.g2y2);
        }
        else if(count[1] == 2 && count[3] == 2){
            img.setImageResource(R.drawable.g2b2);
        }
        else if(count[1] == 2 && count[0] == 1){
            img.setImageResource(R.drawable.r1g2);
        }
        else if(count[1] == 2 && count[2] == 1){
            img.setImageResource(R.drawable.g2y1);
        }
        else if(count[1] == 2 && count[3] == 1){
            img.setImageResource(R.drawable.g2b1);
        }
        else if(count[2] == 3 && count[0] == 1){
            img.setImageResource(R.drawable.r1y3);
        }
        else if(count[2] == 3 && count[1] == 1){
            img.setImageResource(R.drawable.g1y3);
        }
        else if(count[2] == 3 && count[3] == 1){
            img.setImageResource(R.drawable.y3b1);
        }
        else if(count[2] == 2 && count[3] == 2){
            img.setImageResource(R.drawable.y2b2);
        }
        else if(count[2] == 2 && count[0] == 1){
            img.setImageResource(R.drawable.r1y2);
        }
        else if(count[2] == 2 && count[1] == 1){
            img.setImageResource(R.drawable.g1y2);
        }
        else if(count[2] == 2 && count[3] == 1){
            img.setImageResource(R.drawable.y2b1);
        }
        else if(count[3] == 3 && count[0] == 1){
            img.setImageResource(R.drawable.r1b3);
        }
        else if(count[3] == 3 && count[1] == 1){
            img.setImageResource(R.drawable.g1b3);
        }
        else if(count[3] == 3 && count[2] == 1){
            img.setImageResource(R.drawable.y1b3);
        }
        else if(count[3] == 2 && count[0] == 1){
            img.setImageResource(R.drawable.r1b2);
        }
        else if(count[3] == 2 && count[1] == 1){
            img.setImageResource(R.drawable.g1b2);
        }
        else if(count[3] == 2 && count[2] == 1){
            img.setImageResource(R.drawable.y1b2);
        }
        else if(count[0] == 1 && count[1] == 1){
            img.setImageResource(R.drawable.r1g1);
        }
        else if(count[0] == 1 && count[2] == 1){
            img.setImageResource(R.drawable.r1y1);
        }
        else if(count[0] == 1 && count[3] == 1){
            img.setImageResource(R.drawable.r1b1);
        }
        else if(count[1] == 1 && count[2] == 1){
            img.setImageResource(R.drawable.g1y1);
        }
        else if(count[1] == 1 && count[3] == 1){
            img.setImageResource(R.drawable.g1b1);
        }
        else if(count[2] == 1 && count[3] == 1){
            img.setImageResource(R.drawable.y1b1);
        }
        else if(count[0] == 4 ){
            img.setImageResource(R.drawable.red4);
        } else if(count[0] == 3){
            img.setImageResource(R.drawable.red3);
        } else if(count[0] == 2) {
            img.setImageResource(R.drawable.red2);
        } else if(count[0] == 1) {
            img.setImageResource(R.drawable.redc);
        }
        else if(count[1] == 4 ){
            img.setImageResource(R.drawable.green4);
        } else if(count[1] == 3){
            img.setImageResource(R.drawable.green3);
        } else if(count[1] == 2) {
            img.setImageResource(R.drawable.green2);
        } else if(count[1] == 1) {
            img.setImageResource(R.drawable.greenc);
        }
        else if(count[2] == 4 ){
            img.setImageResource(R.drawable.yellow4);
        } else if(count[2] == 3){
            img.setImageResource(R.drawable.yellow3);
        } else if(count[2] == 2) {
            img.setImageResource(R.drawable.yellow2);
        } else if(count[2] == 1) {
            img.setImageResource(R.drawable.yellowc);
        }
        else if(count[3] == 4 ){
            img.setImageResource(R.drawable.blue4);
        } else if(count[3] == 3){
            img.setImageResource(R.drawable.blue3);
        } else if(count[3] == 2) {
            img.setImageResource(R.drawable.blue2);
        } else if(count[3] == 1) {
            img.setImageResource(R.drawable.bluec);
        }
        else {
            img.setImageResource(0);
        }
    }
    public  void getNewPositionId(int tag_no, byte[] count){
        ImageView image;
        switch(tag_no){
            case 0:     image = findViewById(R.id.a2);
                        displayCoins(image,count);
                        break;
            case 8:     image = findViewById(R.id.b7);
                        displayCoins(image,count);
                        break;

            case 13:    image = findViewById(R.id.b6);
                        displayCoins(image,count);
                        break;

            case 21:    image = findViewById(R.id.c4);
                        displayCoins(image,count);
                        break;
            case 26:    image = (findViewById(R.id.c17));
                        displayCoins(image,count);
                        break;

            case 34:    image = (findViewById(R.id.d12));
                        displayCoins(image,count);
                        break;

            case 39:    image = (findViewById(R.id.d13));
                        displayCoins(image,count);
                        break;

            case 47:    image = (findViewById(R.id.a15));
                        displayCoins(image,count);
                        break;
           /* case 0: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a2)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a2)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a2)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a2)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 1: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a3)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a3)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a3)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a3)).setImageResource(R.drawable.bluec);
            }*/
                    image = (findViewById(R.id.a3));
                    displayCoins(image,count);
                    break;

            case 2: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a4)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a4)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a4)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a4)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a4));
                    displayCoins(image,count);
                    break;

            case 3: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a5)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a5)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a5)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a5)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a5));
                    displayCoins(image,count);
                    break;

            case 4: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a6)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a6)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a6)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a6)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a6));
                    displayCoins(image,count);
                    break;

            case 5: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b16)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b16)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b16)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b16)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b16));
                    displayCoins(image,count);
                    break;

            case 6: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b13)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b13)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b13)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b13)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b13));
                    displayCoins(image,count);
                    break;

            case 7: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b10)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b10)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b10)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b10)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b10));
                    displayCoins(image,count);
                    break;

            /*case 8: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b7)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b7)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b7)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b7)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 9: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b4)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b4)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b4)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b4)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b4));
                    displayCoins(image,count);
                    break;

            case 10: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b1)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b1)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b1)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b1)).setImageResource(R.drawable.bluec);
            }
                break;

            case 11: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b2)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b2)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b2)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b2)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b2));
                    displayCoins(image,count);
                    break;

            case 12:/* switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b3)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b3)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b3)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b3)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b3));
                    displayCoins(image,count);
                    break;

            /*case 13: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b6)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b6)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b6)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b6)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 14: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b9)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b9)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b9)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b9)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b9));
                    displayCoins(image,count);
                    break;

            case 15: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b12)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b12)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b12)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b12)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b12));
                    displayCoins(image,count);
                    break;

            case 16: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b15)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b15)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b15)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b15)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b15));
                    displayCoins(image,count);
                    break;

            case 17: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.b18)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.b18)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.b18)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.b18)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.b18));
                    displayCoins(image,count);
                    break;

            case 18:/* switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c1)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c1)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c1)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c1)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c1));
                    displayCoins(image,count);
                    break;

            case 19: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c2)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c2)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c2)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c2)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c2));
                    displayCoins(image,count);
                    break;

            case 20: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c3)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c3)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c3)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c3)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c3));
                    displayCoins(image,count);
                    break;

            /*case 21: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c4)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c4)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c4)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c4)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 22: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c5)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c5)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c5)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c5)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c5));
                    displayCoins(image,count);
                    break;

            case 23: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c6)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c6)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c6)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c6)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c6));
                    displayCoins(image,count);
                    break;

            case 24: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c12)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c12)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c12)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c12)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c12));
                    displayCoins(image,count);
                    break;
            case 25: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c18)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c18)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c18)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c18)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c18));
                    displayCoins(image,count);
                    break;

            /*case 26: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c17)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c17)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c17)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c17)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 27: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c16)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c16)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c16)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c16)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c16));
                    displayCoins(image,count);
                    break;

            case 28:/* switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c15)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c15)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c15)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c15)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c15));
                    displayCoins(image,count);
                    break;

            case 29: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c14)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c14)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c14)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c14)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c14));
                    displayCoins(image,count);
                    break;

            case 30: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.c13)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.c13)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.c13)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.c13)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.c13));
                    displayCoins(image,count);
                    break;

            case 31: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d3)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d3)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d3)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d3)).setImageResource(R.drawable.bluec);
            }
                break;*/
                image = (findViewById(R.id.d3));
                displayCoins(image,count);
                break;

            case 32: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d6)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d6)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d6)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d6)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 33:/* switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d9)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d9)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d9)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d9)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d9));
                    displayCoins(image,count);
                    break;

            /*case 34: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d12)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d12)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d12)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d12)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 35: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d15)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d15)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d15)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d15)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d15));
                    displayCoins(image,count);
                    break;

            case 36: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d18)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d18)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d18)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d18)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d18));
                    displayCoins(image,count);
                    break;

            case 37: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d17)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d17)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d17)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d17)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d17));
                    displayCoins(image,count);
                    break;

            case 38:/* switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d16)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d16)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d16)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d16)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d16));
                    displayCoins(image,count);
                    break;

            /*case 39: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d13)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d13)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d13)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d13)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 40: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d10)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d10)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d10)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d10)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d10));
                    displayCoins(image,count);
                    break;

            case 41: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d7)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d7)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d7)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d7)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d7));
                    displayCoins(image,count);
                    break;

            case 42: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d4)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d4)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d4)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d4)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d4));
                    displayCoins(image,count);
                    break;

            case 43: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.d1)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.d1)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.d1)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.d1)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.d1));
                    displayCoins(image,count);
                    break;

            case 44: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a18)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a18)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a18)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a18)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a18));
                    displayCoins(image,count);
                    break;

            case 45: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a17)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a17)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a17)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a17)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a17));
                    displayCoins(image,count);
                    break;

            case 46: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a16)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a16)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a16)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a16)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a16));
                    displayCoins(image,count);
                    break;

            /*case 47: switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a15)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a15)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a15)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a15)).setImageResource(R.drawable.bluec);
            }
                break;*/

            case 48: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a14)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a14)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a14)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a14)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a14));
                    displayCoins(image,count);
                    break;

            case 49: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a13)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a13)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a13)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a13)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a13));
                    displayCoins(image,count);
                    break;

            case 50: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a7)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a7)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a7)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a7)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a7));
                    displayCoins(image,count);
                    break;

            case 51: /*switch (playerId){
                case 0: ((ImageView)findViewById(R.id.a1)).setImageResource(R.drawable.redc);
                    break;
                case 1: ((ImageView)findViewById(R.id.a1)).setImageResource(R.drawable.greenc);
                    break;
                case 2: ((ImageView)findViewById(R.id.a1)).setImageResource(R.drawable.yellowc);
                    break;
                case 3: ((ImageView)findViewById(R.id.a1)).setImageResource(R.drawable.bluec);
            }
                break;*/
                    image = (findViewById(R.id.a1));
                    displayCoins(image,count);
                    break;
            case 100: //((ImageView)findViewById(R.id.a8)).setImageResource(R.drawable.redc);
                        image = (findViewById(R.id.a8));
                        displayCoins(image,count);
                        break;
            case 101: //((ImageView)findViewById(R.id.a9)).setImageResource(R.drawable.redc);
                        image = (findViewById(R.id.a9));
                        displayCoins(image,count);
                        break;
            case 102: //((ImageView)findViewById(R.id.a10)).setImageResource(R.drawable.redc);
                        image = (findViewById(R.id.a15));
                        displayCoins(image,count);
                        break;
            case 103: //((ImageView)findViewById(R.id.a11)).setImageResource(R.drawable.redc);
                        image = (findViewById(R.id.a11));
                        displayCoins(image,count);
                        break;
            case 104: //((ImageView)findViewById(R.id.a12)).setImageResource(R.drawable.redc);
                        image = (findViewById(R.id.a15));
                        displayCoins(image,count);
                        break;
            case 105:   image = findViewById(R.id.pass1);
                        if(home[0] == 4 ){
                            image.setImageResource(R.drawable.red4);
                        } else if(home[0] == 3){
                            image.setImageResource(R.drawable.red3);
                        } else if(home[0] == 2) {
                            image.setImageResource(R.drawable.red2);
                        } else if(home[0] == 1) {
                            image.setImageResource(R.drawable.redc);
                        }
                        break;
            case 200: //((ImageView)findViewById(R.id.b5)).setImageResource(R.drawable.greenc);
                        image = (findViewById(R.id.b5));
                        displayCoins(image,count);
                        break;
            case 201: //((ImageView)findViewById(R.id.b8)).setImageResource(R.drawable.greenc);
                        image = (findViewById(R.id.b5));
                        displayCoins(image,count);
                        break;
            case 202: //((ImageView)findViewById(R.id.b11)).setImageResource(R.drawable.greenc);
                        image = (findViewById(R.id.b11));
                        displayCoins(image,count);
                        break;
            case 203: //((ImageView)findViewById(R.id.b14)).setImageResource(R.drawable.greenc);
                        image = (findViewById(R.id.b5));
                        displayCoins(image,count);
                        break;
            case 204: //((ImageView)findViewById(R.id.b17)).setImageResource(R.drawable.greenc);
                        image = (findViewById(R.id.b5));
                        displayCoins(image,count);
                        break;
            case 205:   image = findViewById(R.id.pass2);
                        if(home[1] == 4 ){
                            image.setImageResource(R.drawable.green4);
                        } else if(home[1] == 3){
                            image.setImageResource(R.drawable.green3);
                        } else if(home[1] == 2) {
                            image.setImageResource(R.drawable.green2);
                        } else if(home[1] == 1) {
                            image.setImageResource(R.drawable.greenc);
                        }
                        break;
            case 300: //((ImageView)findViewById(R.id.c11)).setImageResource(R.drawable.yellowc);
                        image = (findViewById(R.id.c11));
                        displayCoins(image,count);
                        break;
            case 301: //((ImageView)findViewById(R.id.c10)).setImageResource(R.drawable.yellowc);
                        image = (findViewById(R.id.c10));
                        displayCoins(image,count);
                        break;
            case 302: //((ImageView)findViewById(R.id.c9)).setImageResource(R.drawable.yellowc);
                        image = (findViewById(R.id.c9));
                        displayCoins(image,count);
                        break;
            case 303: //((ImageView)findViewById(R.id.c8)).setImageResource(R.drawable.yellowc);
                        image = (findViewById(R.id.c8));
                        displayCoins(image,count);
                        break;
            case 304: //((ImageView)findViewById(R.id.c7)).setImageResource(R.drawable.yellowc);
                        image = (findViewById(R.id.c7));
                        displayCoins(image,count);
                        break;
            case 305:   image = findViewById(R.id.pass3);
                        if(home[2] == 4 ){
                            image.setImageResource(R.drawable.yellow4);
                        } else if(home[2] == 3){
                            image.setImageResource(R.drawable.yellow3);
                        } else if(home[2] == 2) {
                            image.setImageResource(R.drawable.yellow2);
                        } else if(home[2] == 1) {
                            image.setImageResource(R.drawable.yellowc);
                        }
                        break;
            case 400: //((ImageView)findViewById(R.id.d14)).setImageResource(R.drawable.bluec);
                        image = (findViewById(R.id.d14));
                        displayCoins(image,count);
                        break;
            case 401: //((ImageView)findViewById(R.id.d11)).setImageResource(R.drawable.bluec);
                        image = (findViewById(R.id.d11));
                        displayCoins(image,count);
                        break;
            case 402: //((ImageView)findViewById(R.id.d8)).setImageResource(R.drawable.bluec);
                        image = (findViewById(R.id.d8));
                        displayCoins(image,count);
                        break;
            case 403: //((ImageView)findViewById(R.id.d5)).setImageResource(R.drawable.bluec);
                        image = (findViewById(R.id.d5));
                        displayCoins(image,count);
                        break;
            case 404: //((ImageView)findViewById(R.id.d2)).setImageResource(R.drawable.bluec);
                        image = (findViewById(R.id.d2));
                        displayCoins(image,count);
                        break;
            case 405:
                        image = findViewById(R.id.pass4);
                        if(home[3] == 4 ){
                            image.setImageResource(R.drawable.blue4);
                        } else if(home[3] == 3){
                            image.setImageResource(R.drawable.blue3);
                        } else if(home[3] == 2) {
                            image.setImageResource(R.drawable.blue2);
                        } else if(home[3] == 1){

                            image.setImageResource(R.drawable.bluec);
                        }
                        break;
        }
    }

    public void reposition(int position){
        byte[] count = new byte[]{0, 0, 0, 0};
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(coinPosition[i][j] == position){
                    count[i]+=1;
                }
            }
        }

        getNewPositionId(position,count);
    }
    public void winnerAnnounce(){
        String setter;
        if(playerId == 0){
            winColor[0] = true;
            TextView t1 = findViewById(R.id.t1);
            setter = String.format("%d Red Wins",winPosition);
            t1.setText(setter);
        }
        else if(playerId == 1){
            winColor[1] = true;
            TextView t1 = findViewById(R.id.t2);
            setter = String.format("%d Green Wins",winPosition);
            t1.setText(setter);
        }
        else if(playerId == 2){
            winColor[2] = true;
            TextView t1 = findViewById(R.id.t3);
            setter = String.format("%d Yellow Wins",winPosition);
            t1.setText(setter);
        }
        else{
            winColor[3] = true;
            TextView t1 = findViewById(R.id.t4);
            setter = String.format("%d Blue Wins",winPosition);
            t1.setText(setter);
        }
        if(winPosition == 3){
            activeGame = false;
            ((ImageView)findViewById(R.id.dim2)).setImageResource(0);
            ((ImageView)findViewById(R.id.dim1)).setImageResource(0);
            ((ImageView)findViewById(R.id.dim0)).setImageResource(0);
            ((ImageView)findViewById(R.id.dim3)).setImageResource(0);
                    if(winColor[0] == false){
                        TextView t1 = findViewById(R.id.t1);
                        setter = String.format("Red Loser");
                        t1.setText(setter);
                    }
                    else if(winColor[1] == false){
                        TextView t1 = findViewById(R.id.t2);
                        setter = String.format("Green Loser");
                        t1.setText(setter);
                    }
                    else if(winColor[2] == false){
                        TextView t1 = findViewById(R.id.t3);
                        setter = String.format("Yellow Loser");
                        t1.setText(setter);
                    }
                    else{
                        TextView t1 = findViewById(R.id.t4);
                        setter = String.format("Blue Loser");
                        t1.setText(setter);
                    }
        }
    }
    public void moveStep(View view){
        if(!activeGame) {
            initialState(view);
        }
        else if(activeMove) {
            ImageView img = (ImageView)view;
            int im = Integer.parseInt(img.getTag().toString());

         //  String str1 = valueOf(coinPosition[playerId][pos])+" "+valueOf(coinPosition[playerId][pos+1])+" "+valueOf(coinPosition[playerId][pos+2])+" "+ coinPosition[playerId][pos + 3];
          // Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
            if((coinPosition[playerId][0] == im) || (coinPosition[playerId][1] == im) ||
                    (coinPosition[playerId][2] == im) || (coinPosition[playerId][3] == im)){
                /*if(coinPosition[playerId][0] == im) {
                    coinPosition[playerId][0] = (coinPosition[playerId][0] + rd) % 52;
                }
                else if(coinPosition[playerId][1] == im){
                    coinPosition[playerId][1] = (coinPosition[playerId][1] + rd)%52;
                }
                else if(coinPosition[playerId][2] == im){
                    coinPosition[playerId][2] = (coinPosition[playerId][2] + rd)%52;
                }
                else if(coinPosition[playerId][3] == im){
                    coinPosition[playerId][3] = (coinPosition[playerId][3] + rd)%52;
                }*/
                activeMove = false;
                activeOpen = false;
                /*if(defstop.contains(im)){
                    reposition(im);
                }else {
                    img.setImageResource(0);
                }*/
                if(im>=100){
                            if(rd + (im%100) <= 5){
                                nim = im + rd;
                                if(nim%100 == 5){
                                    home[playerId]+=1;
                                    activeRoll=true;
                                    if(home[playerId] == 4){
                                        activeRoll = false;
                                        winPosition+=1;
                                        winnerAnnounce();
                                    }
                                    setColorStatus(playerId);
                                }
                            }
                            /*else if(nim%100 == 5) {
                                activeMove = true;
                                activeOpen = true;
                            }*/
                            else {
                                    activeMove = true;
                                    nim = im;
                                    if(rd == 6){
                                        activeOpen = true;
                                    }
                            }
                }
                else{
                            if (playerId == 0) {
                                nim = (im + rd);
                                if (im < 51 && nim - 50 > 0) {
                                    nim = (im + rd) - 51 + 100;
                                }
                            } else if (playerId == 1) {
                                nim = (im + rd) % 52;
                                if (im < 12 && nim - 11 > 0) {
                                    nim = (nim - 12) + 200;
                                }
                            } else if (playerId == 2) {
                                nim = (im + rd) % 52;
                                if (im < 25 && nim - 24 > 0) {
                                    nim = (nim - 25) + 300;
                                }
                            } else {
                                nim = (im + rd) % 52;
                                if (im < 38 && nim - 37 > 0) {
                                    nim = (nim - 38) + 400;
                                }
                            }
                            if(nim%100 == 5 && nim>100){
                                home[playerId]+=1;
                                activeRoll = true;
                                if(home[playerId] == 4){
                                    activeRoll = false;
                                    winPosition+=1;
                                    winnerAnnounce();
                                }
                                setColorStatus(playerId);
                            }
                }
                img.setImageResource(0);
                if(coinPosition[playerId][0] == im) {
                    coinPosition[playerId][0] = nim;
                }
                else if(coinPosition[playerId][1] == im){
                    coinPosition[playerId][1] = nim;
                }
                else if(coinPosition[playerId][2] == im){
                    coinPosition[playerId][2] = nim;
                }
                else if(coinPosition[playerId][3] == im){
                    coinPosition[playerId][3] = nim;
                }
                else {

                }
                reposition(im);


                //String str1 = valueOf(nim);
                //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();
                //((ImageView)findViewById(R.id.a10)).setImageResource(R.drawable.redc);

                if(defstop.contains(nim)==false) {
                    int count = 0;
                    for(boolean state:colorStatus){
                        if (state == true && count != playerId) {
                            cutting(view);
                        }
                        count += 1;
                    }
                }

                reposition(nim);

                if(cutCoin || rd==6) {
                    cutCoin = false;
                    activeRoll = true;
                }else {
                    nextMove();
                }
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent checkTTS = new Intent();
       // checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
       // startActivityForResult(checkTTS,MY_DATA);
       // final String State[]={"Red's Turn","Green's Turn","Yellow's Turn","Blue's Turn"};
        myText = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    myText.setLanguage(Locale.UK);
                }
            }
        });
       /* findViewById(R.id.dim0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),State[playerId],Toast.LENGTH_SHORT).show();
                myText.speak(State[0],TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        findViewById(R.id.dim1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),State[playerId],Toast.LENGTH_SHORT).show();
                myText.speak(State[1],TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        findViewById(R.id.dim2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),State[playerId],Toast.LENGTH_SHORT).show();
                myText.speak(State[2],TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        findViewById(R.id.dim3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),State[playerId],Toast.LENGTH_SHORT).show();
                myText.speak(State[3],TextToSpeech.QUEUE_FLUSH,null);
            }
        });*/

    }
}