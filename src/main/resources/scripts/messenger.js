document.addEventListener('DOMContentLoaded', function() {
    const conversationsList = document.getElementById('conversations-list');
    const messagesContainer = document.getElementById('messages-container');
    const emptyState = document.getElementById('empty-state');
    const chatHeader = document.getElementById('chat-header');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');
    const messageInputContainer = document.getElementById('message-input-container');
    const paymentAction = document.getElementById('payment-action');

    let userData = null;
    let activeChat = null;
    let messagePollingInterval = null;

    const colors = {
        'A': 'var(--pink-leaf)',
        'B': 'var(--sleuthe-yellow)',
        'C': 'var(--coral-pink)',
        'D': '#a0d2eb',
        'E': '#7ec4cf',
        'F': '#c5a3ff',
        'G': '#ffb6b9',
        'H': '#bbded6',
        'I': '#fae3d9',
        'J': '#ffb6b9',
        'K': '#61c0bf',
        'L': '#bbded6',
        'M': '#fae3d9',
        'N': '#c5a3ff',
        'O': '#ffb6b9',
        'P': '#bbded6',
        'Q': '#fae3d9',
        'R': '#c5a3ff',
        'S': 'var(--sleuthe-yellow)',
        'T': 'var(--coral-pink)',
        'U': '#bbded6',
        'V': '#fae3d9',
        'W': '#c5a3ff',
        'X': '#ffb6b9',
        'Y': '#bbded6',
        'Z': '#fae3d9'
    };

    initMessenger();

    function initMessenger() {
        fetchMessages();

        setupEventListeners();

        startMessagePolling();
    }

    function setupEventListeners() {
        if (sendButton) {
            sendButton.addEventListener('click', sendMessage);
        }

        if (messageInput) {
            messageInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    sendMessage();
                }
            });
        }

        if (paymentAction) {
            paymentAction.addEventListener('click', function() {
                if (activeChat) {
                    showPaymentDialog(activeChat.Contact);
                }
            });
        }

        window.addEventListener('popstate', function(event) {
            if (event.state && event.state.contact) {
                switchChat(event.state.contact);
            }
        });
    }

    function fetchMessages() {
        fetch(API_ENDPOINTS.getMessages)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                userData = data;
                renderConversations();

                // If there's a conversation in the URL, select it
                const urlParams = new URLSearchParams(window.location.search);
                const contactParam = urlParams.get('contact');

                if (contactParam) {
                    switchChat(contactParam);
                }
            })
            .catch(error => {
                console.error('Error fetching messages:', error);
                // For demo purposes, use example data
                userData = {
                    "Name": "Noah",
                    "Chats": [
                        {
                            "Contact": "James",
                            "Messages": [
                                {
                                    "From": "Noah",
                                    "To": "James",
                                    "Content": "Hello James",
                                    "Time": "23-03-25 03:09"
                                },
                                {
                                    "From": "James",
                                    "To": "Noah",
                                    "Content": "Hello Noah",
                                    "Time": "23-03-25 03:15"
                                }
                            ]
                        },
                        {
                            "Contact": "Naman",
                            "Messages": [
                                {
                                    "From": "Naman",
                                    "To": "Noah",
                                    "Content": "Wassup",
                                    "Time": "23-03-25 03:30"
                                },
                                {
                                    "From": "Noah",
                                    "To": "Naman",
                                    "Content": "Nothing much. So locked in rn",
                                    "Time": "23-03-25 03:45"
                                }
                            ]
                        }
                    ]
                };
                renderConversations();
            });
    }

    /**
     * Start polling for new messages
     */
    function startMessagePolling() {
        // Poll for new messages every 5 seconds
        messagePollingInterval = setInterval(function() {
            fetch(API_ENDPOINTS.getMessages)
                .then(response => response.json())
                .then(data => {
                    // Check if there are new messages
                    if (JSON.stringify(data) !== JSON.stringify(userData)) {
                        // Update userData with new data
                        const previousData = userData;
                        userData = data;

                        // Update UI
                        renderConversations();

                        // If a chat is active, update its messages
                        if (activeChat) {
                            const updatedActiveChat = userData.Chats.find(chat => chat.Contact === activeChat.Contact);
                            if (updatedActiveChat) {
                                // Check if there are new messages in this chat
                                if (updatedActiveChat.Messages.length > activeChat.Messages.length) {
                                    // Update activeChat
                                    activeChat = updatedActiveChat;
                                    // Render only the new messages
                                    renderNewMessages(activeChat, previousData);
                                }
                            }
                        }
                    }
                })
                .catch(error => {
                    console.error('Error polling for messages:', error);
                });
        }, 5000);
    }

    /**
     * Render only new messages that weren't present in the previous data
     */
    function renderNewMessages(currentChat, previousData) {
        const previousChat = previousData.Chats.find(chat => chat.Contact === currentChat.Contact);
        if (!previousChat) return;

        const previousMessagesCount = previousChat.Messages.length;
        const newMessages = currentChat.Messages.slice(previousMessagesCount);

        // Append each new message to the UI
        newMessages.forEach(message => {
            appendMessageToUI(message, currentChat.Contact);
        });

        // Scroll to the bottom
        scrollToBottom();
    }

    /**
     * Render the conversation list
     */
    function renderConversations() {
        if (!userData || !userData.Chats) return;

        // Clear current conversations
        conversationsList.innerHTML = '';

        // Add each conversation to the sidebar
        userData.Chats.forEach(chat => {
            const lastMessage = chat.Messages[chat.Messages.length - 1];
            const avatarColor = getAvatarColor(chat.Contact);
            const isActive = activeChat && activeChat.Contact === chat.Contact;

            // Format time for display
            const time = formatTime(lastMessage.Time);

            const conversationHtml = `
                <div class="conversation ${isActive ? 'active' : ''}" data-contact="${chat.Contact}">
                    <div class="contact-avatar" style="background-color: ${avatarColor}">
                        ${chat.Contact.charAt(0)}
                    </div>
                    <div class="conversation-info">
                        <div class="conversation-header">
                            <div class="contact-name">${chat.Contact}</div>
                            <div class="conversation-time">${time}</div>
                        </div>
                        <div class="conversation-preview">
                            ${lastMessage.Content.substring(0, 40)}${lastMessage.Content.length > 40 ? '...' : ''}
                        </div>
                    </div>
                </div>
            `;

            // Add to the conversations list
            conversationsList.insertAdjacentHTML('beforeend', conversationHtml);
        });

        // Add click listeners to each conversation
        document.querySelectorAll('.conversation').forEach(conversationElement => {
            conversationElement.addEventListener('click', function() {
                const contact = this.getAttribute('data-contact');
                switchChat(contact);
            });
        });
    }

    /**
     * Switch to a different chat
     */
    function switchChat(contactName) {
        if (!userData || !userData.Chats) return;

        // Find the chat in userData
        const chat = userData.Chats.find(c => c.Contact === contactName);
        if (!chat) return;

        // Update active chat
        activeChat = chat;

        // Update URL
        updateUrl(contactName);

        // Update UI
        renderChatHeader(chat);
        renderMessages(chat);
        updateActiveConversation(contactName);

        // Show message input
        messageInputContainer.style.display = 'flex';

        // Focus on the message input
        messageInput.focus();
    }

    /**
     * Render the chat header
     */
    function renderChatHeader(chat) {
        const avatarColor = getAvatarColor(chat.Contact);

        const headerHtml = `
            <div class="chat-contact">
                <div class="contact-avatar" style="background-color: ${avatarColor}">
                    ${chat.Contact.charAt(0)}
                </div>
                <div class="chat-contact-name">
                    <h3>${chat.Contact}</h3>
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

        chatHeader.innerHTML = headerHtml;
    }

    /**
     * Render all messages for a chat
     */
    function renderMessages(chat) {
        // Clear current messages
        messagesContainer.innerHTML = '';

        // Hide empty state
        emptyState.style.display = 'none';

        // Render each message
        chat.Messages.forEach(message => {
            appendMessageToUI(message, chat.Contact);
        });

        // Scroll to the bottom
        scrollToBottom();
    }

    /**
     * Append a single message to the UI
     */
    function appendMessageToUI(message, contactName) {
        const isFromCurrentUser = message.From === userData.Name;
        const time = formatTime(message.Time);

        // Create message elements
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message';

        const messageBubble = document.createElement('div');
        messageBubble.className = `message-bubble ${isFromCurrentUser ? 'sent' : 'received'}`;
        messageBubble.textContent = message.Content;

        // If it's a payment message, add a badge
        if (message.Content.includes('QUBIC')) {
            if (message.Content.includes('Transaction ID:')) {
                const paymentBadge = document.createElement('div');
                paymentBadge.className = 'payment-badge';

                // Extract amount from content
                const amountMatch = message.Content.match(/(\d+(\.\d+)?) QUBIC/);
                if (amountMatch) {
                    paymentBadge.textContent = `${amountMatch[1]} QUBIC Sent ✓`;
                    messageBubble.appendChild(paymentBadge);
                }
            } else if (message.Content.includes('send') && message.Content.includes('QUBIC')) {
                const paymentBadge = document.createElement('div');
                paymentBadge.className = 'payment-badge';
                paymentBadge.textContent = 'Payment Request';
                messageBubble.appendChild(paymentBadge);
            }
        }

        // Add encryption lock icon
        const lockIcon = document.createElement('span');
        lockIcon.className = 'lock-icon';
        lockIcon.textContent = '🔒';
        messageBubble.appendChild(lockIcon);

        // Add timestamp
        const messageTime = document.createElement('span');
        messageTime.className = 'message-time';
        messageTime.textContent = time;

        // Assemble the message
        messageDiv.appendChild(messageBubble);
        messageDiv.appendChild(messageTime);

        // Add to the messages container
        messagesContainer.appendChild(messageDiv);
    }

    /**
     * Update the active conversation in the sidebar
     */
    function updateActiveConversation(contactName) {
        // Remove active class from all conversations
        document.querySelectorAll('.conversation').forEach(conversation => {
            conversation.classList.remove('active');
        });

        // Add active class to the selected conversation
        const conversation = document.querySelector(`.conversation[data-contact="${contactName}"]`);
        if (conversation) {
            conversation.classList.add('active');
        }
    }

    /**
     * Send a new message
     */
    function sendMessage() {
        if (!activeChat) return;

        const messageText = messageInput.value.trim();
        if (!messageText) return;

        // Create message object
        const newMessage = {
            From: userData.Name,
            To: activeChat.Contact,
            Content: messageText,
            Time: formatCurrentTime()
        };

        // Add to UI immediately (optimistic update)
        appendMessageToUI(newMessage, activeChat.Contact);

        // Update local data
        activeChat.Messages.push(newMessage);

        // Update conversation preview
        updateConversationPreview(activeChat.Contact, messageText);

        // Clear input
        messageInput.value = '';

        // Scroll to bottom
        scrollToBottom();

        // Send to API
        sendMessageToAPI(newMessage);
    }

    /**
     * Send a message to the API endpoint
     */
    function sendMessageToAPI(message) {
        fetch(API_ENDPOINTS.newMessage, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(message)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to send message');
                }
                return response.json();
            })
            .then(data => {
                console.log('Message sent successfully:', data);
            })
            .catch(error => {
                console.error('Error sending message:', error);
                // In a production app, handle error (retry, notify user, etc.)
            });
    }

    /**
     * Update the conversation preview in the sidebar
     */
    function updateConversationPreview(contactName, messageText) {
        const conversation = document.querySelector(`.conversation[data-contact="${contactName}"]`);
        if (!conversation) return;

        const previewElement = conversation.querySelector('.conversation-preview');
        const timeElement = conversation.querySelector('.conversation-time');

        if (previewElement) {
            // Truncate preview if needed
            previewElement.textContent = messageText.length > 40
                ? messageText.substring(0, 40) + '...'
                : messageText;
        }

        if (timeElement) {
            // Update time to "Just now"
            timeElement.textContent = 'Just now';
        }

        // Move conversation to the top of the list
        if (conversation.parentElement) {
            conversation.parentElement.prepend(conversation);
        }
    }

    /**
     * Show the payment dialog
     */
    function showPaymentDialog(contactName) {
        // Get the dialog template
        const template = document.getElementById('payment-dialog-template');
        const dialogContainer = template.cloneNode(true);
        dialogContainer.style.display = 'block';

        // Set recipient name
        const recipientElements = dialogContainer.querySelectorAll('.payment-recipient-name');
        recipientElements.forEach(element => {
            element.textContent = contactName;
        });

        // Add to DOM
        document.body.appendChild(dialogContainer);

        // Add event listeners
        const overlay = dialogContainer.querySelector('.payment-dialog-overlay');
        const closeButton = dialogContainer.querySelector('.payment-dialog-close');
        const cancelButton = dialogContainer.querySelector('.payment-cancel');
        const sendButton = dialogContainer.querySelector('.payment-send');

        // Close dialog functions
        const closeDialog = () => {
            document.body.removeChild(dialogContainer);
        };

        // Close dialog events
        overlay.addEventListener('click', function(e) {
            if (e.target === overlay) closeDialog();
        });
        closeButton.addEventListener('click', closeDialog);
        cancelButton.addEventListener('click', closeDialog);

        // Send payment
        sendButton.addEventListener('click', function() {
            const amountInput = dialogContainer.querySelector('#payment-amount');
            const noteInput = dialogContainer.querySelector('#payment-note');

            const amount = parseFloat(amountInput.value);
            const note = noteInput.value.trim();

            if (isNaN(amount) || amount <= 0) {
                alert('Please enter a valid amount');
                return;
            }

            // Generate transaction ID
            const transactionId = 'qb' + Math.random().toString(36).substring(2, 9);

            // Create payment message
            let messageContent = `Sent ${amount} QUBIC`;
            if (note) {
                messageContent += ` for "${note}"`;
            }
            messageContent += `\nTransaction ID: ${transactionId}`;

            // Create and send the message
            const paymentMessage = {
                From: userData.Name,
                To: contactName,
                Content: messageContent,
                Time: formatCurrentTime()
            };

            // Close dialog
            closeDialog();

            // Send the message
            if (activeChat && activeChat.Contact === contactName) {
                // Add to UI immediately
                appendMessageToUI(paymentMessage, contactName);

                // Update local data
                activeChat.Messages.push(paymentMessage);

                // Update conversation preview
                updateConversationPreview(contactName, `${amount} QUBIC Sent`);

                // Scroll to bottom
                scrollToBottom();
            }

            // Send to API
            sendMessageToAPI(paymentMessage);
        });
    }

    /**
     * Update URL without reloading the page
     */
    function updateUrl(contactName) {
        const url = window.location.pathname + '?contact=' + encodeURIComponent(contactName);
        history.pushState({ contact: contactName }, '', url);
    }

    /**
     * Scroll the messages container to the bottom
     */
    function scrollToBottom() {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    /**
     * Get avatar background color based on contact name
     */
    function getAvatarColor(contactName) {
        const firstLetter = contactName.charAt(0).toUpperCase();
        return colors[firstLetter] || 'var(--pink-leaf)';
    }

    function formatTime(timeString) {
        if (!timeString) return '';

        // Assuming format is "YY-MM-DD HH:MM"
        // For simplicity, we'll just extract the HH:MM part
        try {
            const parts = timeString.split(' ');
            if (parts.length >= 2) {
                const timePart = parts[1];
                const [hours, minutes] = timePart.split(':');

                // Convert to 12-hour format
                const hoursNum = parseInt(hours, 10);
                const ampm = hoursNum >= 12 ? 'PM' : 'AM';
                const hours12 = hoursNum % 12 || 12;

                return `${hours12}:${minutes} ${ampm}`;
            }

            // If current date, show time only; if different date, show date
            const messageDate = new Date(timeString);
            const today = new Date();

            if (messageDate.toDateString() === today.toDateString()) {
                return timeString.split(' ')[1];
            } else if (today.getDate() - messageDate.getDate() === 1) {
                return 'Yesterday';
            } else {
                return timeString.split(' ')[0];
            }
        } catch (e) {
            return timeString;
        }
    }

    function formatCurrentTime() {
        const now = new Date();
        const year = now.getFullYear().toString().substr(-2);
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');

        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }

    window.addEventListener('beforeunload', function() {
        if (messagePollingInterval) {
            clearInterval(messagePollingInterval);
        }
    });
});