# Cancer Cell Simulation in Java

This is a basic Java simulation of tumor growth and chemotherapy response in a 2D tissue environment.

## 📌 Description

The simulation models a cross-section of tissue surrounding a vein. It includes:

- Healthy cells and one cancer cell
- Full cell cycle dynamics, with all phases explicitly represented
- Visualization of cell cycle phases through nucleus size and color
- Probabilistic transitions between phases and cell division
- Chemo particles entering the tissue through the vein upon cancer cell division
- Selective killing of mitotic cells (both healthy and cancerous) by chemotherapy

The simulation is visual and runs with a simple GUI.

## ⚙️ How It Works

- Cells change states based on probabilistic rules
- Cancer cell division triggers chemo inflow
- Chemo kills any mitotic-phase cell it contacts

## 💻 How to Run

- Requires Java JDK
- Compile and run:

```bash
javac Part3_final.java
java Part3_final
