# Finance Tracker
Application that takes in bank statements, paystubs, and anything else financially related to track

I want the app as minimal as possible, just an application that you can spin up and run locally where it will have all the financial documents in it that you have already uploaded. Also maintain the state of the application between closes and reopenings.

## Key Features
- Bank Statement PDF Reader
- Paystub PDF Reader
- Manual data entry for values like expenses and income
- Graph visualization of the expenses so far
- 100% Client side, no server processing

## Considerations
I do not think it's easy to have automatic bank statement retrieval to get expenses, you would need to give it constant access to your account which is risky, and there are already things like Mint. So this way it is more secure, no account access, and you can manually change things if there are errors

## Tech Stack (Thinking)
- Typescript
- Vite React
- PDF.js
- Electron
- Some sort of database, like SQL lite
