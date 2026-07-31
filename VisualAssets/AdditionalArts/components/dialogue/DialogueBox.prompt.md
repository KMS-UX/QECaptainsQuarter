Dialogue / system / warning / confirmation message box, with optional pixel portrait and action row.

```jsx
<DialogueBox kind="dialogue" speaker="Lyra" portrait="assets/portrait-lyra.png">
  The readings confirm it. The Core Relay is just ahead.
</DialogueBox>
<DialogueBox kind="confirm" actions={<><Button size="sm">Yes</Button><Button size="sm" variant="ghost">No</Button></>}>Use Quantum Key?</DialogueBox>
```
