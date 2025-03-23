document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements
    const conversations = document.querySelectorAll('.conversation');
    const messagesContainer = document.querySelector('.messages');
    const chatHeader = document.querySelector('.chat-header');
    const messageInput = document.querySelector('.message-input');
    const sendButton = document.querySelector('.send-button');

    // Current active chat
    let activeChat = "alex"; // Default to Alex's chat

    // Initialize the app
    initializeApp();

    /**
     * Initialize the messaging app
     */
    function initializeApp() {
        // Set up conversation click listeners
        setupConversationListeners();

        // Auto-focus the message input
        if (messageInput) {
            messageInput.focus();
        }

        // Set up send button functionality
        setupSendButton();

        // Load the default chat (Alex)
        loadChat(activeChat);
    }

    /**
     * Set up click listeners for all conversation items
     */
    function setupConversationListeners() {
        conversations.forEach(conversation => {
            conversation.addEventListener('click', function() {
                // Get contact name from the conversation element
                const contactName = this.querySelector('.contact-name').textContent.toLowerCase();

                // Handle special case for "Team Blicket" (contains a space)
                const contactKey = contactName === "team blicket" ? "team-blicket" : contactName;

                // Switch to this chat
                if (contactKey in conversationsData) {
                    // Remove active class from all conversations
                    conversations.forEach(c => c.classList.remove('active'));

                    // Add active class to clicked conversation
                    this.classList.add('active');

                    // Clear any unread indicator
                    const unreadIndicator = this.querySelector('.unread');
                    if (unreadIndicator) {
                        unreadIndicator.remove();
                    }

                    // Load this chat
                    loadChat(contactKey);
                }
            });
        });
    }

    /**
     * Set up send button and enter key functionality
     */
    function setupSendButton() {
        if (sendButton && messageInput) {
            sendButton.addEventListener('click', sendMessage);
            messageInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    sendMessage();
                }
            });
        }
    }

    /**
     * Load a specific chat's messages
     */
    function loadChat(chatKey) {
        // Update active chat
        activeChat = chatKey;

        // Get chat data
        const chatData = conversationsData[chatKey];
        if (!chatData) return;

        // Update chat header
        updateChatHeader(chatData);

        // Clear current messages
        if (messagesContainer) {
            messagesContainer.innerHTML = '';
        }

        // Add each message to the UI
        chatData.messages.forEach(message => {
            appendMessage(message);
        });

        // Scroll to the bottom of the messages
        scrollToBottom();
    }

    /**
     * Update the chat header with contact information
     */
    function updateChatHeader(chatData) {
        if (!chatHeader) return;

        // Create the new header content
        const headerHTML = `
            <div class="chat-contact">
                <div class="contact-avatar" style="background-color: ${chatData.avatarColor}">
                    ${chatData.contact.charAt(0)}
                </div>
                <div class="chat-contact-name">
                    <h3>${chatData.contact}</h3>
                </div>
            </div>
            <div class="chat-actions">
                <div class="action-icon">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M3 5H21M3 12H21M3 19H21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </div>
                <div class="action-icon">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M19 11H5M19 11C20.1046 11 21 11.8954 21 13V19C21 20.1046 20.1046 21 19 21H5C3.89543 21 3 20.1046 3 19V13C3 11.8954 3.89543 11 5 11M19 11V9C19 7.89543 18.1046 7 17 7M5 11V9C5 7.89543 5.89543 7 7 7M7 7V5C7 3.89543 7.89543 3 9 3H15C16.1046 3 17 3.89543 17 5V7M7 7H17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </div>
            </div>
        `;

        // Update the header content
        chatHeader.innerHTML = headerHTML;
    }

    /**
     * Send a new message
     */
    function sendMessage() {
        const message = messageInput.value.trim();
        if (!message || !activeChat) return;

        // Get current chat data
        const chatData = conversationsData[activeChat];
        if (!chatData) return;

        // Create message object
        const newMessage = {
            from: "You",
            content: message,
            time: getCurrentTime(),
            isFromCurrentUser: true
        };

        // Add to conversation data
        chatData.messages.push(newMessage);

        // Add to UI
        appendMessage(newMessage);

        // Clear input and scroll to bottom
        messageInput.value = '';
        scrollToBottom();

        // Simulate a reply after a random delay (1-3 seconds)
        simulateReply();
    }

    /**
     * Append a message to the messages container
     */
    function appendMessage(message) {
        if (!messagesContainer) return;

        // Create message elements
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message';

        const messageBubble = document.createElement('div');
        messageBubble.className = `message-bubble ${message.isFromCurrentUser ? 'sent' : 'received'}`;
        messageBubble.textContent = message.content;

        // Add payment badge if applicable
        if (message.isPaymentRequest) {
            const paymentBadge = document.createElement('div');
            paymentBadge.className = 'payment-badge';
            paymentBadge.textContent = 'Payment Request';
            messageBubble.appendChild(paymentBadge);
        } else if (message.isPayment) {
            const paymentBadge = document.createElement('div');
            paymentBadge.className = 'payment-badge';
            paymentBadge.textContent = `${message.amount} QUBIC Sent ✓`;
            messageBubble.appendChild(paymentBadge);
        }

        // Add encryption lock icon
        const lockIcon = document.createElement('span');
        lockIcon.className = 'lock-icon';
        lockIcon.textContent = '🔒';
        messageBubble.appendChild(lockIcon);

        // Add timestamp
        const messageTime = document.createElement('span');
        messageTime.className = 'message-time';
        messageTime.textContent = message.time;

        // Assemble and append the message
        messageDiv.appendChild(messageBubble);
        messageDiv.appendChild(messageTime);
        messagesContainer.appendChild(messageDiv);
    }

    /**
     * Simulate a reply message
     */
    function simulateReply() {
        const chatData = conversationsData[activeChat];
        if (!chatData) return;

        // Random delay between 1-3 seconds
        const replyDelay = 1000 + Math.random() * 2000;

        setTimeout(() => {
            // Generate a contextual reply based on the active chat
            const replyContent = getContextualReply(chatData.contact);

            // Create reply message
            const replyMessage = {
                from: chatData.contact,
                content: replyContent,
                time: getCurrentTime(),
                isFromCurrentUser: false
            };

            // Add to conversation data
            chatData.messages.push(replyMessage);

            // Add to UI
            appendMessage(replyMessage);

            // Scroll to bottom
            scrollToBottom();

            // Update the conversation preview in the sidebar
            updateConversationPreview(activeChat, replyContent);
        }, replyDelay);
    }

    /**
     * Get a contextual reply based on the contact
     */
    function getContextualReply(contact) {
        // Replies tailored to each contact
        const replies = {
            'Alex': [
                "Thanks for your message! I'll get back to you soon.",
                "Got it, I'll follow up on that shortly.",
                "I appreciate the update. Let me check my schedule.",
                "Good to hear from you! Let's discuss this further tomorrow."
            ],
            'Sarah': [
                "Perfect! I'll have those designs ready for you soon.",
                "Thanks for the feedback. I'll work on those adjustments.",
                "Let me know if you need anything else for the project.",
                "I'll send over the updated files by end of day."
            ],
            'Team Blicket': [
                "Thanks for using Blicket! Can we help with anything else?",
                "We're glad you're enjoying the platform!",
                "Don't hesitate to reach out if you have any questions.",
                "Your feedback helps us improve. Thanks for sharing!"
            ],
            'David': [
                "Sounds good. I'll update the project timeline.",
                "Great! Let's discuss the details in our meeting.",
                "I'll prepare the necessary documentation.",
                "Thanks for confirming. Looking forward to our collaboration."
            ],
            'Mai': [
                "Thanks for the update on the integration.",
                "I'll check the blockchain status and get back to you.",
                "Let me know how the test transactions are working on your end.",
                "I've updated the smart contract. Can you review it when you have time?"
            ]
        };

        // Get replies for this contact, or use default replies
        const contactReplies = replies[contact] || [
            "Thanks for your message!",
            "I'll get back to you soon.",
            "Got it, thanks for letting me know.",
            "I'll check on that and update you."
        ];

        // Return a random reply from the available options
        return contactReplies[Math.floor(Math.random() * contactReplies.length)];
    }

    /**
     * Update the conversation preview in the sidebar
     */
    function updateConversationPreview(chatKey, messageText) {
        const contactName = conversationsData[chatKey].contact;

        // Find the conversation element
        let conversationElement = null;
        conversations.forEach(conv => {
            const name = conv.querySelector('.contact-name').textContent;
            if (name === contactName) {
                conversationElement = conv;
            }
        });

        if (!conversationElement) return;

        // Update preview text
        const previewElement = conversationElement.querySelector('.conversation-preview');
        if (previewElement) {
            previewElement.textContent = messageText.length > 40
                ? messageText.substring(0, 40) + '...'
                : messageText;
        }

        // Update timestamp
        const timeElement = conversationElement.querySelector('.conversation-time');
        if (timeElement) {
            timeElement.textContent = 'Just now';
        }
    }

    /**
     * Get current formatted time (12-hour format with AM/PM)
     */
    function getCurrentTime() {
        const now = new Date();
        let hours = now.getHours();
        const minutes = now.getMinutes().toString().padStart(2, '0');
        const ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12; // Convert 0 to 12
        return `${hours}:${minutes} ${ampm}`;
    }

    /**
     * Scroll the messages container to the bottom
     */
    function scrollToBottom() {
        if (messagesContainer) {
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }
    }
});


/**
 * This script adds payment functionality to the messenger
 * It should be included after the main messages.js file
 */
document.addEventListener('DOMContentLoaded', function() {
    // Find payment action button
    const paymentActionButtons = document.querySelectorAll('.input-action:nth-child(3)');

    // Setup payment action listeners
    paymentActionButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Get current active chat from main script
            if (typeof activeChat !== 'undefined') {
                const chatData = conversationsData[activeChat];
                if (chatData) {
                    showPaymentDialog(chatData.contact);
                }
            }
        });
    });

    /**
     * Show the payment dialog for a recipient
     */
    function showPaymentDialog(recipientName) {
        // Get the dialog template
        const template = document.getElementById('payment-dialog-template');
        if (!template) return;

        // Clone the template
        const dialogContainer = template.cloneNode(true);
        dialogContainer.style.display = 'block';
        dialogContainer.id = ''; // Remove the ID to avoid duplicates

        // Set recipient name in all places
        const recipientElements = dialogContainer.querySelectorAll('.payment-recipient-name');
        recipientElements.forEach(el => {
            el.textContent = recipientName;
        });

        // Add to DOM
        document.body.appendChild(dialogContainer);

        // Get UI elements
        const overlay = dialogContainer.querySelector('.payment-dialog-overlay');
        const closeButton = dialogContainer.querySelector('.payment-dialog-close');
        const cancelButton = dialogContainer.querySelector('.payment-cancel');
        const sendButton = dialogContainer.querySelector('.payment-send');
        const amountInput = dialogContainer.querySelector('#payment-amount');
        const noteInput = dialogContainer.querySelector('#payment-note');

        // Focus on amount input
        if (amountInput) {
            amountInput.focus();
        }

        // Function to close the dialog
        const closeDialog = () => {
            document.body.removeChild(dialogContainer);
        };

        // Setup event listeners
        overlay.addEventListener('click', function(e) {
            if (e.target === overlay) closeDialog();
        });

        closeButton.addEventListener('click', closeDialog);
        cancelButton.addEventListener('click', closeDialog);

        // Handle payment send
        sendButton.addEventListener('click', function() {
            // Validate amount
            const amount = parseFloat(amountInput.value);
            if (isNaN(amount) || amount <= 0) {
                alert('Please enter a valid amount');
                return;
            }

            // Get note if provided
            const note = noteInput.value.trim();

            // Create message content
            const transactionId = 'qb' + Math.random().toString(36).substr(2, 7);
            let messageContent = `Sent! Transaction ID: ${transactionId}`;

            // Create a new message
            const paymentMessage = {
                from: 'You',
                content: messageContent,
                time: getCurrentTime(),
                isFromCurrentUser: true,
                isPayment: true,
                amount: amount
            };

            // Close the dialog
            closeDialog();

            // Add message to conversation data and UI
            if (typeof activeChat !== 'undefined' && conversationsData[activeChat]) {
                const chatData = conversationsData[activeChat];

                // Add to data
                chatData.messages.push(paymentMessage);

                // Add to UI - use the appendMessage function from main script
                if (typeof appendMessage === 'function') {
                    appendMessage(paymentMessage);
                } else {
                    // Fallback if appendMessage isn't available
                    const messagesContainer = document.querySelector('.messages');
                    if (messagesContainer) {
                        // Create message elements
                        const messageDiv = document.createElement('div');
                        messageDiv.className = 'message';

                        const messageBubble = document.createElement('div');
                        messageBubble.className = 'message-bubble sent';
                        messageBubble.textContent = messageContent;

                        // Add payment badge
                        const paymentBadge = document.createElement('div');
                        paymentBadge.className = 'payment-badge';
                        paymentBadge.textContent = `${amount} QUBIC Sent ✓`;
                        messageBubble.appendChild(paymentBadge);

                        // Add lock icon
                        const lockIcon = document.createElement('span');
                        lockIcon.className = 'lock-icon';
                        lockIcon.textContent = '🔒';
                        messageBubble.appendChild(lockIcon);

                        // Add timestamp
                        const messageTime = document.createElement('span');
                        messageTime.className = 'message-time';
                        messageTime.textContent = getCurrentTime();

                        // Assemble message
                        messageDiv.appendChild(messageBubble);
                        messageDiv.appendChild(messageTime);
                        messagesContainer.appendChild(messageDiv);

                        // Scroll to bottom
                        messagesContainer.scrollTop = messagesContainer.scrollHeight;
                    }
                }

                // Update conversation preview
                const conversationElement = document.querySelector(`.conversation.active`);
                if (conversationElement) {
                    const previewElement = conversationElement.querySelector('.conversation-preview');
                    if (previewElement) {
                        previewElement.textContent = messageContent;
                    }

                    const timeElement = conversationElement.querySelector('.conversation-time');
                    if (timeElement) {
                        timeElement.textContent = 'Just now';
                    }
                }

                // Simulate a reply after a delay
                setTimeout(() => {
                    // Create reply message
                    const replyMessage = {
                        from: chatData.contact,
                        content: `Thanks for the ${amount} QUBIC payment! I've received it.`,
                        time: getCurrentTime(),
                        isFromCurrentUser: false
                    };

                    // Add to data
                    chatData.messages.push(replyMessage);

                    // Add to UI
                    if (typeof appendMessage === 'function') {
                        appendMessage(replyMessage);
                    } else {
                        // Fallback
                        const messagesContainer = document.querySelector('.messages');
                        if (messagesContainer) {
                            const replyDiv = document.createElement('div');
                            replyDiv.className = 'message';

                            const replyBubble = document.createElement('div');
                            replyBubble.className = 'message-bubble received';
                            replyBubble.textContent = replyMessage.content;

                            const replyLock = document.createElement('span');
                            replyLock.className = 'lock-icon';
                            replyLock.textContent = '🔒';
                            replyBubble.appendChild(replyLock);

                            const replyTime = document.createElement('span');
                            replyTime.className = 'message-time';
                            replyTime.textContent = getCurrentTime();

                            replyDiv.appendChild(replyBubble);
                            replyDiv.appendChild(replyTime);
                            messagesContainer.appendChild(replyDiv);

                            messagesContainer.scrollTop = messagesContainer.scrollHeight;
                        }
                    }
                }, 2000);
            }
        });
    }

    /**
     * Get current formatted time (12-hour format with AM/PM)
     */
    function getCurrentTime() {
        const now = new Date();
        let hours = now.getHours();
        const minutes = now.getMinutes().toString().padStart(2, '0');
        const ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12; // Convert 0 to 12
        return `${hours}:${minutes} ${ampm}`;
    }
});