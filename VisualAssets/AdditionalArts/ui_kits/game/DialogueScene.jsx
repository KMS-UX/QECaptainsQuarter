function DialogueScene({onBack}){
  const {DialogueBox,Button}=window.DS;
  const lines=[
    {s:'Lyra',p:'../../assets/portrait-lyra.png',k:'dialogue',t:"The readings confirm it — the Core Relay is just ahead. Whatever the Void Seekers want with it, we can't let them reach it first."},
    {s:'Aurin',p:'../../assets/portrait-aurin.png',k:'dialogue',t:"Then we move now. Every second we wait, the rift widens."},
    {s:'Lyra',p:'../../assets/portrait-lyra.png',k:'dialogue',t:"Careful. Chronarch Prime knows we're coming. This choice will echo further than you know."},
  ];
  const [i,setI]=React.useState(0);
  const [choice,setChoice]=React.useState(false);
  return <window.Backdrop image="../../assets/keyart-lab.png" dim={0.55}>
    <div style={{position:'absolute',inset:0,display:'flex',flexDirection:'column',justifyContent:'flex-end',padding:28,gap:14}}>
      <div style={{position:'absolute',top:20,right:20}}><Button variant="ghost" size="sm" onClick={onBack}>Skip ▸</Button></div>
      <div style={{maxWidth:760,margin:'0 auto',width:'100%'}}>
        <DialogueBox kind={lines[i].k} speaker={lines[i].s} portrait={lines[i].p}>{lines[i].t}</DialogueBox>
        {!choice ? <div style={{display:'flex',justifyContent:'flex-end',marginTop:10}}>
          <Button size="sm" onClick={()=>{ if(i<lines.length-1) setI(i+1); else setChoice(true); }}>{i<lines.length-1?'Continue ▸':'Respond ▸'}</Button>
        </div> : <div style={{display:'flex',flexDirection:'column',gap:8,marginTop:12,maxWidth:520}}>
          <div style={{fontFamily:'var(--font-display)',fontSize:10,letterSpacing:'.14em',color:'var(--teal)'}}>CHOOSE YOUR RESPONSE</div>
          <Button variant="secondary" size="sm" onClick={onBack}>"I'll bring the relay online. No matter the cost."</Button>
          <Button variant="secondary" size="sm" onClick={onBack}>"And if the cost is too high?"</Button>
          <Button variant="ghost" size="sm" onClick={onBack}>[ Say nothing ]</Button>
        </div>}
      </div>
    </div>
  </window.Backdrop>;
}
window.DialogueScene=DialogueScene;
