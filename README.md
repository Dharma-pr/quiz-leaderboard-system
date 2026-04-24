# Quiz Leaderboard System

## Overview

This project is based on a backend integration task where I worked with an external API that provides quiz data across multiple rounds. The main challenge was that the API could return duplicate data in different calls, so the goal was to handle those duplicates correctly and still generate an accurate leaderboard.

## What I Did

* Collected quiz data by calling the API 10 times (poll 0 to 9)
* Maintained a delay between each request as required
* Removed duplicate entries using a combination of roundId and participant
* Calculated total scores for each participant
* Sorted the results to generate the final leaderboard
* Submitted the final leaderboard to the API

## How It Works

Each API call returns a set of events containing participant scores. Since the same data can appear in multiple responses, I used a set to track unique entries based on (roundId + participant) so that duplicates are ignored.

After filtering duplicates, I used a map to store and update the total score of each participant. Finally, I sorted the participants based on their total scores in descending order to generate the leaderboard.

## Tech Used

* Java
* Maven
* Java HTTP Client
* Jackson Library for JSON parsing

## How to Run

```bash
mvn compile
mvn exec:java
```

## Output

* Leaderboard with participant scores
* Total combined score of all participants
* API response confirming successful submission

## Project Structure

```
quiz-leaderboard-system/
 └── quiz-leaderboard-final/
      ├── pom.xml
      └── src/main/java/QuizLeaderboard.java
```

## Notes

* Duplicate API responses are handled to avoid incorrect scoring
* A delay of 5 seconds is maintained between API calls
* The submission is performed only once

## Author

Reg No: RA2311003020348
