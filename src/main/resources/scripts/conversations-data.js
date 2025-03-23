// Conversation data for all chats
const conversationsData = {
    "alex": {
        contact: "Alex",
        avatarColor: "var(--pink-leaf)",
        messages: [
            {
                from: "Alex",
                content: "Hey, need to send you those files securely.",
                time: "10:23 AM",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Perfect timing! I'm online now.",
                time: "10:24 AM",
                isFromCurrentUser: true
            },
            {
                from: "Alex",
                content: "Also, can you transfer 10 QUBIC for the project?",
                time: "10:26 AM",
                isFromCurrentUser: false,
                isPaymentRequest: true
            },
            {
                from: "You",
                content: "Sent! Transaction ID: qb7f9d2e5",
                time: "10:27 AM",
                isFromCurrentUser: true,
                isPayment: true,
                amount: 10
            },
            {
                from: "Alex",
                content: "Perfect! I'll send over the files now.",
                time: "10:28 AM",
                isFromCurrentUser: false
            },
            {
                from: "Alex",
                content: "I've attached the documents. They're encrypted with your public key.",
                time: "10:29 AM",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Got them, thanks! I'll take a look and get back to you by end of day.",
                time: "10:30 AM",
                isFromCurrentUser: true
            }
        ]
    },
    "sarah": {
        contact: "Sarah",
        avatarColor: "var(--sleuthe-yellow)",
        messages: [
            {
                from: "Sarah",
                content: "Hi there! Did you get a chance to look at those design mockups I sent over last week?",
                time: "Yesterday 14:22 PM",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Yes, I reviewed them this morning. The new dashboard layout looks great!",
                time: "Yesterday 15:45 PM",
                isFromCurrentUser: true
            },
            {
                from: "Sarah",
                content: "Awesome! Glad you like it. Any specific feedback or changes you'd like to see?",
                time: "Yesterday 16:03 PM",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "I think the color scheme works well, but I'm wondering if we should make the buttons more prominent?",
                time: "Yesterday 16:15 PM",
                isFromCurrentUser: true
            },
            {
                from: "Sarah",
                content: "Good point. I'll work on a version with more visible call-to-actions.",
                time: "Yesterday 16:30 PM",
                isFromCurrentUser: false
            },
            {
                from: "Sarah",
                content: "I'll have the updated files ready by tomorrow. Should I send them here?",
                time: "Yesterday 16:35 PM",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "That would be perfect.",
                time: "Yesterday 17:01 PM",
                isFromCurrentUser: true
            },
            {
                from: "Sarah",
                content: "Let me know when you've received the files",
                time: "Yesterday 17:15 PM",
                isFromCurrentUser: false
            }
        ]
    },
    "team-blicket": {
        contact: "Team Blicket",
        avatarColor: "var(--coral-pink)",
        messages: [
            {
                from: "Team Blicket",
                content: "Welcome to Blicket! We're excited to have you join our secure messaging platform.",
                time: "2 days ago",
                isFromCurrentUser: false
            },
            {
                from: "Team Blicket",
                content: "Your account has been successfully set up with end-to-end encryption. All your messages are secured with quantum-resistant cryptography.",
                time: "2 days ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Thanks for the welcome! I'm excited to try out the platform.",
                time: "2 days ago",
                isFromCurrentUser: true
            },
            {
                from: "Team Blicket",
                content: "Great to hear! As a blockchain-based platform, Blicket allows you to send QUBIC tokens directly through chats. Would you like a quick tour of the features?",
                time: "2 days ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Yes, that would be helpful. I'm particularly interested in learning more about the payment system.",
                time: "2 days ago",
                isFromCurrentUser: true
            },
            {
                from: "Team Blicket",
                content: "Perfect! To send a payment, simply click the payment icon in the message input area. You can attach a note and specify the amount. All transactions are processed on the Qubic blockchain.",
                time: "2 days ago",
                isFromCurrentUser: false
            },
            {
                from: "Team Blicket",
                content: "If you have any questions or need assistance, feel free to message us anytime. Happy messaging!",
                time: "2 days ago",
                isFromCurrentUser: false
            }
        ]
    },
    "david": {
        contact: "David",
        avatarColor: "#a0d2eb",
        messages: [
            {
                from: "David",
                content: "Hey, are we still on for the project meeting next Tuesday?",
                time: "1 week ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Yes, Tuesday at 2 PM works for me. Do you want to meet at the office or virtually?",
                time: "1 week ago",
                isFromCurrentUser: true
            },
            {
                from: "David",
                content: "Let's do it virtually. I'll set up a link and send it over.",
                time: "1 week ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Sounds good. Have you finished reviewing the latest project specs?",
                time: "1 week ago",
                isFromCurrentUser: true
            },
            {
                from: "David",
                content: "Almost done. There are a few points I want to discuss during our meeting.",
                time: "1 week ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Great, I've also got some updates on the timeline.",
                time: "1 week ago",
                isFromCurrentUser: true
            },
            {
                from: "David",
                content: "Perfect. Also, do you think we need to involve the design team at this stage?",
                time: "1 week ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "I think we should wait until we have the core functionality mapped out.",
                time: "1 week ago",
                isFromCurrentUser: true
            },
            {
                from: "David",
                content: "Makes sense. Thanks for the update. Let's meet next week.",
                time: "1 week ago",
                isFromCurrentUser: false
            }
        ]
    },
    "mai": {
        contact: "Mai",
        avatarColor: "#7ec4cf",
        messages: [
            {
                from: "Mai",
                content: "Hello! I'm working on that decentralized app we discussed. Do you have time to chat about the integration?",
                time: "2 weeks ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Hi Mai! Yes, I'd be happy to discuss the integration. What's your main concern?",
                time: "2 weeks ago",
                isFromCurrentUser: true
            },
            {
                from: "Mai",
                content: "I'm trying to determine the best way to connect our app to the Qubic blockchain. I need your public key for testing transactions.",
                time: "2 weeks ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Sure, here's my public key: QBIC_PK_72x9a3b5c7d4e8f1g2h5j6k8l9m0n1p2q3r4s5t",
                time: "2 weeks ago",
                isFromCurrentUser: true
            },
            {
                from: "Mai",
                content: "Thanks! I'll set up the test environment and try sending a small transaction.",
                time: "2 weeks ago",
                isFromCurrentUser: false
            },
            {
                from: "Mai",
                content: "By the way, have you had a chance to look at the smart contract code I shared last week?",
                time: "2 weeks ago",
                isFromCurrentUser: false
            },
            {
                from: "You",
                content: "Not yet, but I'll get to it tomorrow and send you my feedback.",
                time: "2 weeks ago",
                isFromCurrentUser: true
            },
            {
                from: "Mai",
                content: "No rush. I'm just curious about your thoughts on the security features.",
                time: "2 weeks ago",
                isFromCurrentUser: false
            },
            {
                from: "Mai",
                content: "I'm having trouble importing your key. Can you send me your public key again?",
                time: "2 weeks ago",
                isFromCurrentUser: false
            }
        ],
        unreadCount: 3
    }
};

