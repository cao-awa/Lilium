const f = () => {
    const { getKernel } = require('falcon-sign');
    (async () =>
    {
        let Falcon1024 = await getKernel('falcon1024_n3_v1'); //get falcon512_n3_v1 Kernel
        //gernkey
        let keypair = Falcon1024.genkey(); //return { sk, pk, genKeySeed }
        //sign
        let text = 'TEST MSG';
        let sign = Falcon1024.sign(text, keypair.sk);
        keypair = Falcon1024.genkey(keypair.genkeySeed)
        //verify
        console.log(Falcon1024.verify(sign, text, keypair.pk));
        //create public key by private key
        let pk = Falcon1024.publicKeyCreate(keypair.sk);0
    })();

    return <p>w</p>
}

export default function MainPage() {
    return <p>
        aaa {f()}
    </p>
}