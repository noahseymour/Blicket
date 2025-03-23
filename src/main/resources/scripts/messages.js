// Simple script to make the conversation items clickable
document.addEventListener('DOMContentLoaded', function() {
    const conversations = document.querySelectorAll('.conversation');

    conversations.forEach(conversation => {
        conversation.addEventListener('click', function() {
            // Remove active class from all conversations
            conversations.forEach(c => c.classList.remove('active'));
            // Add active class to clicked conversation
            this.classList.add('active');
        });
    });

    // Auto-focus the message input
    document.querySelector('.message-input').focus();

    // Make the send button functional
    const sendButton = document.querySelector('.send-button');
    const messageInput = document.querySelector('.message-input');
    const messagesContainer = document.querySelector('.messages');

    sendButton.addEventListener('click', sendMessage);
    messageInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    function sendMessage() {
        const message = messageInput.value.trim();
        if (message) {
            // Create message elements
            const messageDiv = document.createElement('div');
            messageDiv.className = 'message';

            const messageBubble = document.createElement('div');
            messageBubble.className = 'message-bubble sent';
            messageBubble.textContent = message;

            const lockIcon = document.createElement('span');
            lockIcon.className = 'lock-icon';
            lockIcon.textContent = '🔒';

            const messageTime = document.createElement('span');
            messageTime.className = 'message-time';

            // Get current time
            const now = new Date();
            let hours = now.getHours();
            const minutes = now.getMinutes().toString().padStart(2, '0');
            const ampm = hours >= 12 ? 'PM' : 'AM';
            hours = hours % 12;
            hours = hours ? hours : 12; // Convert 0 to 12
            const timeString = `${hours}:${minutes} ${ampm}`;

            messageTime.textContent = timeString;

            // Append elements
            messageBubble.appendChild(lockIcon);
            messageDiv.appendChild(messageBubble);
            messageDiv.appendChild(messageTime);
            messagesContainer.appendChild(messageDiv);

            // Clear input and scroll to bottom
            messageInput.value = '';
            messagesContainer.scrollTop = messagesContainer.scrollHeight;

            // Simulate a reply after 1 second
            setTimeout(() => {
                const replyDiv = document.createElement('div');
                replyDiv.className = 'message';

                const replyBubble = document.createElement('div');
                replyBubble.className = 'message-bubble received';
                replyBubble.textContent = 'Got your message! I\'ll respond shortly.';

                const replyLockIcon = document.createElement('span');
                replyLockIcon.className = 'lock-icon';
                replyLockIcon.textContent = '🔒';

                const replyTime = document.createElement('span');
                replyTime.className = 'message-time';
                replyTime.textContent = timeString;

                replyBubble.appendChild(replyLockIcon);
                replyDiv.appendChild(replyBubble);
                replyDiv.appendChild(replyTime);
                messagesContainer.appendChild(replyDiv);

                messagesContainer.scrollTop = messagesContainer.scrollHeight;
            }, 1000);
        }
    }
});