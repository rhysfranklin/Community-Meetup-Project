# Community-Meetup-Project


#create docker container
docker run --name postgres-db -e POSTGRES_PASSWORD=docker -p 5432:5432 -d postgres

Start the App with Two Env Variables
The default constructor expects the following two env variables exist when starting the app.

Env Variable	Description
SLACK_BOT_TOKEN	The valid bot token value starting with xoxb- in your development workspace. To issue a bot token, you need to install your Slack App that has a bot user to your development workspace. Visit the Slack App configuration page, choose the app you’re working on, and go to Settings > Install App on the left pane (Add app_mentions:read bot scope if you see the message saying “Please add at least one feature or permission scope to install your app.”).

If you run an app that is installable for multiple workspaces, no need to specify this. Consult App Distribution (OAuth) for further information instead.
SLACK_SIGNING_SECRET	The secret value shared only with the Slack Platform. It is used for verifying incoming requests from Slack. Request verification is crucial for security as Slack apps have internet-facing endpoints. To know the value, visit the Slack App configuration page, choose the app you’re working on, go to Settings > Basic Information on the left pane, and find App Credentials > Signing Secret on the page. Refer to the document for further information.
