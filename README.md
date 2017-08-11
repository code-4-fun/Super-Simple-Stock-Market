# Super-Simple-Stock-Market
Implementation for Simple Stock Market Problem Statement

### Problem Statement

---

1. Provide working source code that will :-

    - For a given stock,
        - Given any price as input, calculate the dividend yield
        - Given any price as input, calculate the P/E Ratio
        - Record a trade, with timestamp, quantity of shares, buy or sell indicator and traded price
        - Calculate Volume Weighted Stock Price based on trades in past 15 minutes
        
    b. Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
 

### Solution

---

##### Tech Stack
I tried to keep the solution very basic and minimal. The tech stack I used is as follows;

- Source: Java - 8
- Test Framework: [Spock Framework](Spock Framework) and Groovy - 2.4.10
- Build System: Gradle - 3.5

##### How to build and run test cases

- To build the source;
    ```
    ./gradlew clean build
    ```
    
    This command will build the source, execute test specifications and prepare execution report. This report can be accessed at;
    > build/reports/tests/test/index.html