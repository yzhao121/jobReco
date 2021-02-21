# jobReco
A Personalized Job Recommendation Engine

/***The instance has been terminated because AWS charges.***/

The project aims to use personalization to improve job search and recommendation. We designed and implemented an interactive web application for users to search and apply available job positions.

**Front End**

1. Developed an interactive web page for users to search and apply positions (HTML, CSS, JavaScript, AJAX);

2. Used favorite records to provide personalized position recommendation.

**Back End**

1. Created three Java servlets with RESTful APIs to handle HTTP requests and responses. The first one is a SearchItems API that provides the functionality to search around, The second servlet that allows a user to set or unset their favorite records. The last one that recommends similar items to the user. And these servlets are deployed on tomcat;

2. Built MySQL database on Amazon RDS to store position data from Github API;

3. Used MonkeyLearn API to extract keywords from description of positions;

4. Designed algorithms (e.g., content-based recommendation) to implement job recommendation;

5. Deployed to Amazon EC2 for better performance.
