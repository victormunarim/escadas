export const obterLinkDownload = (link: string): string => {
    if (!link) return '';
    if (link.includes('/file/d/')) {
        const partes = link.split('/file/d/');
        if (partes.length > 1) {
            const id = partes[1].split('/')[0];
            return `https://drive.google.com/uc?export=download&id=${id}`;
        }
    }
    return link;
};
