function ExplorationHUD({onOpenQuests,onOpenInventory,onTalk}){
  const {Panel,StatBar,ItemSlot,Notification,ObjectiveList,Button}=window.DS;
  return <window.Backdrop image="../../assets/keyart-neosolis.png" dim={0.5}>
    <div style={{position:'absolute',inset:0,padding:20}}>
      {/* top-left player plate */}
      <div style={{position:'absolute',top:20,left:20,width:250}}>
        <Panel title="Aurin · Chronomancer · Lv. 23">
          <div style={{display:'flex',flexDirection:'column',gap:7}}>
            <StatBar kind="hp" label="HP" value={430} max={520}/>
            <StatBar kind="ep" label="EP" value={65} max={80}/>
            <StatBar kind="xp" label="XP" value={38} max={100}/>
          </div>
        </Panel>
      </div>
      {/* top-right minimap + currency */}
      <div style={{position:'absolute',top:20,right:20,width:180,display:'flex',flexDirection:'column',gap:10}}>
        <Panel accent="var(--atom-gold)" corners={false} style={{padding:0,overflow:'hidden'}}>
          <img src="../../assets/keyart-neosolis.png" alt="map" style={{width:'100%',height:120,objectFit:'cover',filter:'hue-rotate(-10deg) saturate(1.2)',imageRendering:'pixelated'}}/>
          <div style={{padding:'6px 10px',fontFamily:'var(--font-display)',fontSize:9,letterSpacing:'.12em',color:'var(--atom-gold)',borderTop:'1px solid var(--border-panel)'}}>NEO-SOLIS · DISTRICT 7</div>
        </Panel>
        <div style={{display:'flex',justifyContent:'space-between',fontSize:12,padding:'0 4px'}}>
          <window.Coin>12,450</window.Coin><window.Shard>318</window.Shard>
        </div>
      </div>
      {/* objective tracker */}
      <div style={{position:'absolute',top:230,right:20,width:230}}>
        <ObjectiveList title="The Core Relay" items={[
          {text:"Reach the Core Relay",count:"0/1"},
          {text:"Override Security Node",count:"1/1",done:true},
          {text:"Defend the terminal",count:"2/5"},
        ]}/>
      </div>
      {/* notification */}
      <div style={{position:'absolute',top:230,left:20}}>
        <Notification items={["New Quest: The Core Relay","Discovered: District 7"]}/>
      </div>
      {/* bottom-center skill bar */}
      <div style={{position:'absolute',bottom:20,left:'50%',transform:'translateX(-50%)',display:'flex',gap:8,alignItems:'center'}}>
        <ItemSlot rarity="epic" image={window.ICON+'skill-fire.png'} quality="Q"/>
        <ItemSlot rarity="rare" image={window.ICON+'skill-frost.png'} quality="W"/>
        <ItemSlot rarity="legendary" image={window.ICON+'skill-nova.png'} quality="E"/>
        <ItemSlot rarity="uncommon" image={window.ICON+'skill-slash.png'} quality="R"/>
        <ItemSlot rarity="common" image={window.ICON+'potion-health.png'} quality="F"/>
      </div>
      {/* bottom-right actions */}
      <div style={{position:'absolute',bottom:20,right:20,display:'flex',gap:8}}>
        <Button variant="secondary" size="sm" onClick={onTalk}>Talk</Button>
        <Button variant="secondary" size="sm" onClick={onOpenQuests}>Quests</Button>
        <Button variant="secondary" size="sm" onClick={onOpenInventory}>Inventory</Button>
      </div>
      {/* interaction prompt */}
      <div style={{position:'absolute',bottom:110,left:'50%',transform:'translateX(-50%)',fontSize:12,color:'var(--teal)',letterSpacing:'.08em',textShadow:'0 0 8px #000'}}>[ E ] Speak with Lyra</div>
    </div>
  </window.Backdrop>;
}
window.ExplorationHUD=ExplorationHUD;
