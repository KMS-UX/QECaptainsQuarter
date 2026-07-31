function QuestLog({onBack}){
  const {Panel,QuestCard,Tabs,Button,ObjectiveList}=window.DS;
  const [tab,setTab]=React.useState('Main');
  return <div style={{position:'absolute',inset:0,background:'var(--surface-page)',backgroundImage:'var(--scanline)',padding:28,display:'flex',flexDirection:'column',gap:16}}>
    <div style={{display:'flex',alignItems:'center',justifyContent:'space-between'}}>
      <h1 style={{fontSize:22,color:'var(--atom-gold)',margin:0}}>Quest Log</h1>
      <Button variant="ghost" size="sm" onClick={onBack}>← Back</Button>
    </div>
    <Tabs tabs={["Main","Side","Faction","World","Completed"]} active={tab} onChange={setTab}/>
    <div style={{display:'flex',gap:16,flex:1,minHeight:0}}>
      <div style={{display:'flex',gap:14,flexWrap:'wrap',alignContent:'flex-start'}}>
        <QuestCard tier="main" title="The Core Relay" image="../../assets/keyart-neosolis.png" description="The relay's signal leads to the heart of Quantum." objective="Reach the Core Relay" progress={0} progressMax={1} level="18+" footer="Neo-Solis"/>
        <QuestCard tier="side" title="Missing Convoy" image="../../assets/keyart-wasteland.png" description="A merchant convoy vanished in the Ashfields." progress={1} progressMax={3} level="16" footer="Ashfields"/>
        <QuestCard tier="faction" title="Echoes of the Void" image="../../assets/keyart-lab.png" description="The Void Seekers request your aid at the rift." progress={2} progressMax={5} level="20" footer="Void Seekers"/>
      </div>
      <div style={{width:260,flex:'none'}}>
        <Panel title="Tracked · The Core Relay" accent="var(--quest-main)">
          <p style={{fontSize:12,color:'var(--text-muted)',lineHeight:1.5,marginTop:0}}>"The Core Relay is our last line of defense. Get it online, and keep it safe. Quantum depends on you."</p>
          <ObjectiveList items={[
            {text:"Reach the Core Relay",count:"0/1"},
            {text:"Override Security Node",count:"1/1",done:true},
            {text:"Defend the terminal",count:"2/5"},
          ]}/>
          <div style={{marginTop:12,fontFamily:'var(--font-display)',fontSize:10,letterSpacing:'.12em',color:'var(--text-faint)'}}>REWARDS</div>
          <div style={{display:'flex',gap:16,marginTop:6,fontSize:12}}><window.Coin>2,500</window.Coin><window.Shard>40</window.Shard><span style={{color:'var(--rarity-epic)'}}>◆ Chrono Cell</span></div>
        </Panel>
      </div>
    </div>
  </div>;
}
window.QuestLog=QuestLog;
