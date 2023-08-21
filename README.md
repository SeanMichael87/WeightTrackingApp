# New You - Weight Tracker App

The New You app aims to address users' needs for effective weight management and goal tracking. By allowing users to create profiles, input daily weights, and set weight loss goals, the app provides a comprehensive solution for monitoring their weight-related progress. With features like goal setting, goal date tracking, and SMS notifications, users can stay motivated and focused on their fitness objectives. The app empowers users to make informed decisions about their health and wellness by providing a user-friendly platform to track their weight journey and celebrate milestones.

## Description

My project involved developing a comprehensive weight tracker app to address users' weight management and fitness goals. The app offered a user-centric experience through multiple screens and features. Users could sign up with secure accounts, input daily weights, set weight loss goals, and manage their profiles. The app's home dashboard provided a snapshot of their progress, including current weight, goal weight, days to goal date, and weight difference. The UI design focused on simplicity and clarity, with intuitive icons and clear labels guiding users through interactions. Personalization was key; users could enable SMS notifications for motivational messages upon reaching milestones. The project's success stemmed from its user-centered approach, driven by usability testing and feedback incorporation. New You's design aimed to simplify weight tracking, empower users to achieve their goals, and celebrate their accomplishments, making it an effective tool for personal health and fitness management.

## Approach

When creating the New You app, I followed a structured approach that encompassed understanding user requirements, meticulous planning, and thoughtful design. I emphasized modular development to break down the app into manageable components. I also employed version control to track changes through the weeks, and prioritized testing and debugging to ensure a robust and bug-free app. The user's perspective was central to my design process, so I focused on intuitive navigation and user-friendly interfaces. By documenting code and seeking feedback from testers (my wife and family), I aimed to create a user-centered and reliable app. These practices can be incredibly valuable as I move forward in future projects and is essential for agile methodologies, responsive design, automated testing, continuous integration, and maintaining open feedback for continuous improvement. Applying these strategies will help me foster the creation of user-centric, adaptable, and well-tested applications.

## Testing

Manual testing was a huge part of my process to ensure proper functionality of my app. This involved myself and unbiases testers navigating the UI in order to find certain buggy behavior. I did what I could to break the code and handle any exceptions that could occur. For instance, with the sign up activity, I implemented limits to each input to prevent null data that would affect functionality of the database and usefulness of the app. Testing this way helped provide insights into data accuracy, performance issues, and real-world user feedback, ultimately enhancing the app's quality and usability. This process not only ensured the app's stability and responsiveness but also established a strong foundation for continuous development and improvement.

## Struggles

There were many different areas that had its own learning curve that I had to overcome. First, was implementing the Date Picker funcitonality. It was an important aspect that I really wanted to add to my app. However, I was struggling to implement it within another Dialog as part of the sign up. Experimentation, researching documents and forums such as StackOverflow helped me get past this hurdle and provide the interface as part of my project. Another area was implementing a GraphView as part of my UI. There is no built in functionality for this in Android Studio, so I had to research different libraries and ways to implement this functionality in my app. At first, I used jjoe from Github, but I found the documentation difficult to follow and implement. I therefore switched to MPAndroidChart, which had its own difficuties to get working but was easier to implement the weight history data. 

## Successes

The areas I was most successful implementing, I feel, are the database and the shared preference handler. When developing the database, I had to figure out a way to store the user's individual weight data. I had to do so in a way that is efficient and not prone to data issues. Therefore, I went with a seperate weight table that was linked to the profile datatable via a unique user id. I then implemented a shared preference handle that would store the user id upon login authentication. This unique id would then be used to query specific data based on the logged in user.
## Getting Started

### Dependencies

**Android OS Version**: The app is developed for Android devices. It requires a compatible Android operating system version, typically Android 9.0 (Pie/API 28) or higher.

**Permissions**: The app may require permissions to access features such as SMS (for sending congratulatory messages).

**Libraries and Dependencies**: The app may utilize libraries for UI components (like RecyclerView, EditText), date handling (like DatePickerDialog), and SMS management (like SmsManager). These libraries are typically included as dependencies in the app's build.gradle file.

### Executing program
1. **Device Compatibility**:
Make sure you have an Android device or emulator with a compatible version of the Android operating system (Android 9.0 Pie or higher).
2. **Installation**:
Download the New You weight tracker app's installation package (APK file) from a trusted source or platform.
3. **Permissions**:
During installation, the app might request various permissions, such as accessing SMS, storage, and network. Grant the necessary permissions for the app to function properly.
4. **Launch the App**:
Locate the app icon on your device's home screen or app drawer.
Tap on the app icon to launch it.
5. **Sign Up or Log In**:
If you're a new user, you'll need to sign up for an account.
Provide a username, password, phone number, and other required details.
If you're an existing user, log in with your username and password.
6. **Dashboard**:
Upon successful login, you'll be taken to the app's home dashboard.
Here, you'll see your current weight, goal weight, days to goal date, and weight difference.
7. **Weight Tracking**:
Tap on the appropriate option to input your daily weight.
8. **Profile Management**:
Access the profile management screen to update your account details, such as username, password, and phone number.
You may also adjust settings like SMS notifications.
9. **Congratulatory SMS**:
If enabled, the app will send you congratulatory SMS messages when you achieve your weight goals.
10. **Navigation**:
Use the app's navigation features, such as buttons or tabs, to move between different screens and features.
11. **Log Out**:
When you're done using the app, make sure to log out to ensure the security of your account and data.


## Authors

Sean Bruyere

Sean.Bruyere@snhu.edu

Sean.Bruy21@gmail.com

Version History
0.1

