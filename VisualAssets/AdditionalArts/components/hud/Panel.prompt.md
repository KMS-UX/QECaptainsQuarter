Base container for every HUD surface — dark, scanlined, corner-ticked.

```jsx
<Panel title="Current Objective">
  <div>Find the Core Relay</div>
</Panel>
```

`accent` recolors title + corner ticks (e.g. `var(--atom-gold)` for reward panels); `corners={false}` for plain boxes.
