const chatMessages = document.getElementById('chatMessages');
const chatInput = document.getElementById('chatInput');
const sendButton = document.getElementById('sendButton');

sendButton.addEventListener('click', () => {
  const message = chatInput.value.trim();
  if (message) {
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    chatMessages.appendChild(messageElement);
    chatInput.value = '';
    chatMessages.scrollTop = chatMessages.scrollHeight; // Auto-scroll
  }
});

chatInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') {
    sendButton.click();
  }
});
