# Question
```
    Design a system that lets users chat with an AI assistant for customer support, with the ability to escalate to a human agent when needed.
    The system should support text-based conversations, retrieve relevant knowledge base articles to ground responses, and handle both AI and human agents seamlessly within the same conversation thread.
    Requirements to clarify with the candidate:
    Functional:
    • Users send messages and receive AI-generated responses in real time (streaming).
    • The AI should ground its answers in a company knowledge base (RAG - retrieval augmented generation).
    • Support multi-turn conversations with memory of prior messages.
    Non-functional:
    • Low latency for first-token response (~1-2s).
    • Scale to millions of concurrent conversations.
    • High availability — support can’t go down.
    • Cost control (LLM calls are expensive)(bonus)
```

Steps:
1. Hydration: loads state in to memory
2. Normalizer: speech to text
3. Classify
4. FastpathResponse/RAG/agen loop when unclear

# Embedding Model
Provided by each LLM Provider
0Tied to the llm model
