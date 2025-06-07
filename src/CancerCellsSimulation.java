

import nano.Canvas;
import nano.Pen;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class CancerCellsSimulation {

    public CancerCellsSimulation() {
        // Begin of code for final exercise
        int xSize = 640;
        int ySize = 640;
        Canvas screen = new Canvas(xSize, ySize, 0, 0);
//each cell phase has a color
//cancer cell has red boundary
//each cell phase has correct duration
//cancer cells have a faster cell cycle
//nuclues grows with every cell phase
        Random random = new Random();
        int cells = 10000; //max cell number
        int celln = 50; //initial cell number
        int outer_radius = 10; //cell outer radius not nucleus radius
        int xpos[] = new int[cells]; //array storing x position of each cell
        int ypos[] = new int[cells]; //array storing y position of each cell
        int cellcycle[] = new int[cells]; //array tracking cell cycle phase of each cell
        int nuc_radius[] = new int[cells]; //nucleus radius of each cell
        int divisions[] = new int[cells]; //stores division count of each cell
        int dx[] = new int[cells]; //change in xpos of each cell
        int dy[] = new int[cells]; //change in ypos of each cell
        boolean[] type = new boolean[cells]; //stores if cancer of healthy cell
        boolean[] exist = new boolean[cells]; //keeps track if cell exists
        Color color[] = new Color[cells]; //nucleus color for cell phases
        Color colorb[] = new Color[cells]; //border to distinguish between cancer and healthy cell

        int chemo_max = 1000; //max chemo num
        int chemon = 500; //number of chemo particles
        int chemo_radius = 2; //radius of chemo particle
        int xchem[] = new int[chemo_max]; //array for x pos of chemo particles
        int ychem[] = new int[chemo_max]; //array for y pos of chemo particles
        int dxchem[] = new int[chemo_max]; //change in xpos of each chemo particle
        int dychem[] = new int[chemo_max]; //change in xpos of each chemo particle
        int cancercounter = 1; //counter for number of cancer cells
        int chemocounter = 0; //counter for number of chemo particles
        boolean[] existchemo = new boolean[chemo_max]; //keeps track of whether chemo particles exist
        Pen pen = new Pen(screen);
        int k = 0;

        //initialising cancer and healthy cells
        for (int i = 0; i < celln; i++) {
            type[i] = true; // true = healthy, false = cancer
            exist[i] = true;
            divisions[i] = 0;
            type[celln - 1] = false; // initialises cancer cells; 1 cancer out of 100 cells initialised

            if (type[i]) {
                colorb[i] = Color.WHITE; //white border = healthy
            }
            else {
                colorb[i] = Color.RED; //red border = cancer
            }

            //initial position of each cell (while loop is to prevent overlap of cells)
            int initcount=1; //if 0 means no overlap will occur, otherwise xpos and ypos have to be reinitialised
            while(initcount!=0){
                initcount =0;
                xpos[i] = random.nextInt(outer_radius, xSize - outer_radius);

                if (type[i]) {
                    int chance = random.nextInt(2); //either above or below blood vessel for healthy cells
                    if (chance == 0) {
                        ypos[i] = random.nextInt(outer_radius, 270 - outer_radius);
                    }
                    else {
                        ypos[i] = random.nextInt(370 + outer_radius, ySize - outer_radius);
                    }
                }
                else { //cancer cells can spawn anywhere in canvas
                    ypos[i] = random.nextInt(outer_radius, xSize - outer_radius);
                }
                for(int j =0;j < i;j++) {  //compares one cell to all other existing cells
                    double d = Math.sqrt(Math.pow(xpos[i] - xpos[j], 2) + Math.pow(ypos[i] - ypos[j], 2));
                    if (d < outer_radius * 2) {
                        initcount++; //if overlap would occur, initcount is incremented
                    }
                }
            }
        }

        //initialisation of chemo
        for (int x = 0; x < chemon; x++) {
            existchemo[x] = true;
            xchem[x] = -2;
            ychem[x] = random.nextInt(270 + chemo_radius, 370 - chemo_radius);
        }

//Determining cell cycle of each cell, chances for starting in a certain cell phase are all equal:
//G1=0=Green, S=1=Red, G2=2=Yellow, M=3=Magenta, G0=4=Grey
//Nucleus radius changes for each cell phase
        for (int i = 0; i < celln; i++) {
            cellcycle[i] = random.nextInt(0, 4);
            if (cellcycle[i] == 0) {
                color[i] = Color.GREEN;
                nuc_radius[i] = 5;
            }
            if (cellcycle[i] == 1) {
                color[i] = Color.RED;
                nuc_radius[i] = 6;
            }
            if (cellcycle[i] == 2) {
                color[i] = Color.YELLOW;
                nuc_radius[i] = 7;
            }
            if (cellcycle[i] == 3) {
                color[i] = Color.MAGENTA;
                nuc_radius[i] = 8;
            }
        }



//Movement of cells
        while (k == 0) {

            for (int i = 0; i < celln; i++) {

                dx[i] = random.nextInt(-2, 3);
                dy[i] = random.nextInt(-2, 3);

                if (xpos[i] + dx[i] > xSize - outer_radius || xpos[i] + dx[i] < outer_radius) {dx[i] = 0;}  //prevent cell (healthy & cancer) from leaving screen in x direction

                if (type[i]) { //healthy
                    if (ypos[i] >= 370 + outer_radius) { //above blood vessel, prevent cell leaving screen in y direction
                        if (ypos[i] + dy[i] >= ySize - outer_radius || ypos[i] + dy[i] <= 370 + outer_radius) {dy[i] = 0;}
                    }

                    if (ypos[i] <= 270 - outer_radius) {//below blood vessel, prevent cell leaving screen in y direction
                        if (ypos[i] + dy[i] <= outer_radius || ypos[i] + dy[i] >= 270 - outer_radius) {dy[i] = 0;}
                    }
                }
                else { //cancer
                    if (ypos[i] + dy[i] >= ySize - outer_radius || ypos[i] + dy[i] <= outer_radius) {dy[i] = 0;} //prevent cancer cell leaving screen in y direction
                }

                for (int j = 0; j < celln; j++) {//to compare one cell to all other cells
                    if (i != j) { // to account for comparing a cell to itself then don't need to change the direction
                        double d = Math.sqrt(Math.pow((xpos[i]+dx[i])- xpos[j], 2) + Math.pow((ypos[i]+dy[i])- ypos[j], 2));
                        if (d < outer_radius * 2) { //to prevent overlap
                            dx[i]=0;
                            dy[i]=0;
                        }
                    }
                }

                xpos[i] = xpos[i] + dx[i]; // change the x,y coordinates of cell
                ypos[i] = ypos[i] + dy[i];


                //cell phase progression of healthy cells
                //shorter cell phase have a higher chance to go into the next cell phase
                if (type[i]) {
                    if (cellcycle[i] == 0) { //G1 -> S
                        int chanceg1 = random.nextInt(0, 900);
                        if (chanceg1 == 1) {
                            cellcycle[i] = 1;
                            color[i] = Color.RED;
                            nuc_radius[i] = 6;
                        }
                    }
                    if (cellcycle[i] == 1) {//S -> G2
                        int chanceS = random.nextInt(0, 600);
                        if (chanceS == 1) {
                            cellcycle[i] = 2;
                            color[i] = Color.YELLOW;
                            nuc_radius[i] = 7;
                        }
                    }
                    if (cellcycle[i] == 2) {//G2 -> M
                        int chanceg2 = random.nextInt(0, 300);
                        if (chanceg2 == 1) {
                            cellcycle[i] = 3;
                            color[i] = Color.MAGENTA;
                            nuc_radius[i] = 8;
                        }

                    }

                    if (cellcycle[i] == 3) {//M -> G1 or G0
                        int chanceM = random.nextInt(0, 75);
                        if (chanceM == 1) {
                            int count_cells_1 = 0;
                            for (int m = 0; m < celln; m++) {
                                double d = Math.sqrt(Math.pow(xpos[i] - xpos[m], 2) + Math.pow(ypos[i] - ypos[m], 2)); //check distance between one cell and all others
                                if (d > outer_radius * 4 + outer_radius/2.0) {
                                    count_cells_1++; //if enough space, increment counter
                                }
                            }
                            //celln-1 is to account for comparing to same cell, and second condition is to ensure dividing cell is within enough space of screen
                            if (count_cells_1 == celln - 1
                                    && xpos[i] > outer_radius * 4 //enough distance from the side of the screen to divide  (x direction)
                                    && xpos[i] < 640 - outer_radius * 4 //enough distance from the side of the screen to divide (x direction)
                                    && ((ypos[i] < ySize - outer_radius * 4 //enough distance from the side of the screen to divide ( y direction)
                                    && ypos[i] > 370 + outer_radius * 4) //enough distance from the side of the screen to divide ( from the vessel)
                                    || (ypos[i] < 270 - outer_radius * 4 // //enough distance from the side of the screen to divide (from the vessel)
                                    && ypos[i] > outer_radius * 4))) { //enough distance from the side of the screen to divide ( y direction)

                                //dividing cell conditions change
                                cellcycle[i] = 0;
                                color[i] = Color.GREEN;
                                nuc_radius[i] = 5;
                                divisions[i]++; //incremented for dividing cell


                                //calculating coordinates of new cell
                                xpos[celln] = random.nextInt(xpos[i] - 20, xpos[i] + 20);
                                double temp = Math.sqrt(Math.pow(2 * outer_radius, 2) - Math.pow(xpos[i] - xpos[celln], 2)); //temp is distance that the cell centre of the new cell will
                                //be down from the initial cell, appropriate with the determined x position
                                int temp1 = (int) (Math.round(temp)); // squared and roots can only go in doubles, so we transform it into an integer
                                int up_downchance = random.nextInt(0,2); //determines whether new cell spans above or below initial cell
                                if (up_downchance == 0) {
                                    ypos[celln] = ypos[i] - temp1;
                                }
                                if (up_downchance == 1) {
                                    ypos[celln] = ypos[i] + temp1;
                                }


                                //characteristics of new healthy cell
                                nuc_radius[celln] = 5;
                                cellcycle[celln] = 0;
                                color[celln] = Color.GREEN;
                                colorb[celln] = Color.WHITE;
                                exist[celln] = true;
                                type[celln] = true;
                                celln++;
                            }
                            //if not enough space, dividing goes into G0 and stays there indefinitely
                            else {
                                color[i] = Color.LIGHT_GRAY;
                                nuc_radius[i] = 3;
                                cellcycle[i]= 4;
                            }

                            //If cell divided 5 times, it dies (the cell at the end (number celln) g=becomes the died cell at number i
                            if (divisions[i] == 5) {
                                xpos[i] = xpos[celln];
                                ypos[i] = ypos[celln];
                                nuc_radius[i] = nuc_radius[celln];
                                cellcycle[i] = cellcycle[celln];
                                exist[i] = exist[celln];
                                type[i] = type[celln];
                                color[i] = color[celln];
                                divisions[i] = divisions[celln];
                                divisions[celln] = 0;
                                exist[celln] = false;
                                celln--;
                            }
                        }
                    }
                }



                //cell phase progression of cancer cells
                if (!type[i]) {
                    if (cellcycle[i] == 0) {//G1 -> S
                        int chanceg1 = random.nextInt(0, 30);
                        if (chanceg1 == 1) {
                            cellcycle[i] = 1;
                            color[i] = Color.RED;
                            nuc_radius[i] = 6;
                        }
                    }
                    if (cellcycle[i] == 1) { //S -> G2
                        int chanceS = random.nextInt(0, 20);
                        if (chanceS == 1) {
                            cellcycle[i] = 2;
                            color[i] = Color.YELLOW;
                            nuc_radius[i] = 7;
                        }
                    }
                    if (cellcycle[i] == 2) {//G2 -> M
                        int chanceg2 = random.nextInt(0, 10);
                        if (chanceg2 == 1) {
                            cellcycle[i] = 3;
                            color[i] = Color.MAGENTA;
                            nuc_radius[i] = 8;
                        }
                    }
                    if (cellcycle[i] == 3) {//M -> G1 or G2
                        int chanceM = random.nextInt(0, 75);
                        if (chanceM == 1) {
                            int count_cells_2 = 0;
                            for (int m = 0; m < celln; m++) {
                                double d = Math.sqrt(Math.pow(xpos[i] - xpos[m], 2) + Math.pow(ypos[i] - ypos[m], 2));
                                if (d > outer_radius * 4) {//check if enough space
                                    count_cells_2++;
                                }
                            }

                            if (count_cells_2 == celln - 1
                                    && xpos[i] + dx[i] > outer_radius * 4
                                    && xpos[i] + dx[i] < 640 - outer_radius * 4
                                    && ypos[i] + dy[i] < ySize - outer_radius * 4
                                    && ypos[i] + dy[i] > outer_radius * 4) {

                                //characteristics of dividing cell changes
                                cellcycle[i] = 0;
                                color[i] = Color.GREEN;
                                nuc_radius[i] = 5;
                                divisions[i]++;

                                //determining coordinates of new cancer cell
                                xpos[celln] = random.nextInt(xpos[i] - 20, xpos[i] + 20);
                                double temp = Math.sqrt(Math.pow(2 * outer_radius, 2) - Math.pow(xpos[i] - xpos[celln], 2));
                                int temp1 = (int) Math.round(temp);
                                int up_downchance = random.nextInt(0,2); //determines whether new cell spans above or below initial cell
                                if (up_downchance == 0) {
                                    ypos[celln] = ypos[i] - temp1;
                                }
                                if (up_downchance == 1) {
                                    ypos[celln] = ypos[i] + temp1;
                                }

                                //characteristics of new cancer cell
                                nuc_radius[celln] = 5;
                                cellcycle[celln] = 0;
                                color[celln] = Color.GREEN;
                                colorb[celln] = Color.RED;
                                exist[celln] = true;
                                type[celln] = false;
                                celln++;
                                cancercounter++;


                            }
                            else {//if not enough space -> G2
                                color[i] = Color.YELLOW;
                                nuc_radius[i] = 7;
                                cellcycle[i]=2;
                            }
                        }
                    }

                }
            }

            //chemo particle movement

            if(chemocounter>0) {
                for(int a=0; a<chemon; a++){

                    //determine if chemo moves above or below blood vessel
                    int chance = random.nextInt(2);
                    if (chance == 0) {dychem[a] = random.nextInt(7, 9);} // if we did an intiger from -8 to 8 it wuld stay in the blood vessel to much and go out again)
                    if (chance == 1) {dychem[a] = random.nextInt(-8, -6);}

                    if (ychem[a] + dychem[a] >= ySize - chemo_radius || ychem[a] + dychem[a] <= chemo_radius) {dychem[a] = 0;} //keep chemo within screen in y direction

                    if (ychem[a] < 370 - chemo_radius && ychem[a] > 270 + chemo_radius) {dxchem[a] = 3;} //in blood vessel, chemo flows with the blood stream

                    if (ychem[a] > 370 + chemo_radius || ychem[a] < 270 - chemo_radius) {dxchem[a] = random.nextInt(-2, 3);}//dx movement if outside blood vessel

                    if (xchem[a] + dxchem[a] > xSize - chemo_radius || xchem[a] + dxchem[a] < chemo_radius) {//chemo particles can only leave through vessel
                        if (ychem[a] + dychem[a] > 370 + chemo_radius || ychem[a] + dychem[a] < 270 - chemo_radius) {
                            dxchem[a] = 0;
                        }
                    }
                }

                for (int a = 0; a < chemon; a++) {
                    if (existchemo[a]) {
                        boolean removechemo = false; //if true then chemocounter-1
                        for (int i = 0; i < celln; i++) { //compare one chemo particle to all cells
                            double d = Math.sqrt(Math.pow(xpos[i] - xchem[a], 2) + Math.pow(ypos[i] - ychem[a], 2)); //calculate distance between cell and chemo particle
                            if (cellcycle[i] == 3 && d < outer_radius - chemo_radius) {//if cell in mitosis and chemo fully inside cell, chemo and cell dies
                                if (!type[i]) {
                                    cancercounter--; //if cell is a cancer cell, cancer counter goes down
                                }

                                //chemo characteristics change
                                existchemo[a] = false;
                                removechemo = true;

                                //cell characteristics change (last cell becomes cell number i)
                                xpos[i] = xpos[celln];
                                ypos[i] = ypos[celln];
                                nuc_radius[i] = nuc_radius[celln];
                                cellcycle[i] = cellcycle[celln];
                                exist[i] = exist[celln];
                                exist[celln] = false;
                                divisions[i] = divisions[celln];
                                divisions[celln] = 0;
                                type[i] = type[celln];
                                colorb[i] = colorb[celln];
                                color[i] = color[celln];
                                celln--;
                            }

                        }

                        //chance of degradation of chemo particles
                        int chemodeg = random.nextInt(0, 500);
                        if (chemodeg == 1) {
                            existchemo[a] = false;
                            removechemo = true;
                        }

                        if (xchem[a] >= xSize) {//if chemo leaves screen, considered degraded
                            removechemo = true;
                            existchemo[a] = false;
                        }
                        if (removechemo) {
                            chemocounter--;
                        }

                        xchem[a] = xchem[a] + dxchem[a];//change position of chemo particles
                        ychem[a] = ychem[a] + dychem[a];
                    }
                }
            }

            else if (chemocounter==0 && cancercounter>1){ //if chemo gone but still have cancer cells, chemo reinitialised
                chemocounter=chemon;
                for (int x = 0; x < chemon; x++) {
                    existchemo[x] = true;
                    xchem[x] = -2;
                    ychem[x] = random.nextInt(270 + chemo_radius, 370 - chemo_radius);
                }
            }



            //drawing

            pen.drawRectangle(0, 270, 640, 100, Color.PINK, true); //blood vessel

            for (int i = 0; i < celln; i++) {
                if (exist[i]) {
                    pen.drawCircle(xpos[i], ypos[i], outer_radius, colorb[i], false);
                    pen.drawCircle(xpos[i], ypos[i], nuc_radius[i], color[i], true); //true is filled circle, the nucleus
                }
            }
            for (int i = 0; i < chemon; i++) {
                if (existchemo[i]) {
                    pen.drawCircle(xchem[i], ychem[i], chemo_radius, Color.CYAN, true);
                }
            }

            //update screen
            screen.update();
            screen.pause(50);
            screen.clear();

        }




        //add method(s) below this line
    }

    public static void main(String[] args) {
        CancerCellsSimulation e = new CancerCellsSimulation();
    }
}
