#Assignment in Mobile Application Development - Part 2 

*Tech_press_fot*
News platform for University students and staffs

## Technologies Used
    Firebase Authentication: For secure user sign-up, login, and managing user sessions.
    Firebase Firestore: NoSQL cloud database used to store and retrieve news articles dynamically.
        Collection Name: `news_items`
        Document Fields:
            * `title` (String): Title of the news article.
            * `body` (String): Main content of the news article.
            * `date` (String): Publication date(e.g., "YYYY-MM-DD").
            * `type` (String): Category  (e.g., "Sports", "Academic", "Faculty Events").
            * `imageUrl` (String): URL of the image.

    Glide (com.github.bumptech.glide)**: For efficient and fast image loading from URLs into `ImageViews`.
    Material Design Components**: For building a modern and user-friendly interface (e.g., `TextInputLayout`, `MaterialButton`, `BottomNavigationView`, `DialogFragment`).
