Radar minimap for the exploration & combat HUD (HUD & UI Kit §09).

```jsx
<Minimap shape="circular" label="Sector 7" blips={[
  {kind:'player',x:0.5,y:0.5},
  {kind:'objective',x:0.7,y:0.3},
  {kind:'enemy',x:0.35,y:0.6},
]}/>
```

`shape`: `circular` (default, compass ring + N), `square`, or `fog`. Set `fog` to overlay the fog-of-war fade on any shape. Marker colors come from `--marker-*` tokens (player green, ally teal, enemy red, objective gold, poi blue).
